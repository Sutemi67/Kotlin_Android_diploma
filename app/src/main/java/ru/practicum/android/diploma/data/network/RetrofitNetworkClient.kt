package ru.practicum.android.diploma.data.network

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.data.dto.AllVacancyRequest
import ru.practicum.android.diploma.data.dto.Response
import ru.practicum.android.diploma.data.dto.VacancyRequest
import java.io.IOException

class RetrofitNetworkClient(
    private val api: HhApi,
    private val connectManager: ConnectManager
) : NetworkClient {

    private val token = "Bearer ${BuildConfig.HH_ACCESS_TOKEN}"

    override suspend fun doSearchRequest(dto: Any): Response {
        return when {
            !connectManager.isConnected() -> createErrorResponse(ERROR_NO_CONNECTION)
            dto !is AllVacancyRequest && dto !is VacancyRequest -> createErrorResponse(ERROR_INVALID_DTO)
            else -> withContext(Dispatchers.IO) {
                try {
                    val response = executeApiRequest(dto)
                    response.apply { resultCode = SUCCESS }
                } catch (e: IOException) {
                    Log.e("TAG", "Network error", e)
                    createErrorResponse(ERROR_IO_EXCEPTION)
                }
            }
        }
    }

    private suspend fun executeApiRequest(dto: Any): Response {
        return when (dto) {
            is AllVacancyRequest -> api.searchVacancies(
                token = token,
                query = dto.expression,
                page = dto.page
            )
            is VacancyRequest -> api.getVacancyDetails(token = token, dto.id)
            else -> error("Unexpected dto type: ${dto::class}")
        }
    }

    private fun createErrorResponse(errorCode: Int): Response {
        return Response().apply { resultCode = errorCode }
    }

    companion object {
        private const val ERROR_NO_CONNECTION = -1
        private const val ERROR_INVALID_DTO = 400
        private const val ERROR_IO_EXCEPTION = 500
        private const val SUCCESS = 200
    }
}
