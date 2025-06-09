package ru.practicum.android.diploma.data.network

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.data.dto.AllVacancyRequest
import ru.practicum.android.diploma.data.dto.Response
import ru.practicum.android.diploma.data.dto.VacancyRequest
import ru.practicum.android.diploma.domain.network.models.Area
import ru.practicum.android.diploma.domain.network.models.Industry
import java.io.IOException

class RetrofitNetworkClient(
    private val api: HhApi,
    private val connectManager: ConnectManager
) : NetworkClient {

    private val token = "Bearer ${BuildConfig.HH_ACCESS_TOKEN}"

    override suspend fun doSearchRequest(dto: AllVacancyRequest): Response {
        return when {
            !connectManager.isConnected() -> createErrorResponse(ERROR_NO_CONNECTION)
            else -> withContext(Dispatchers.IO) {
                try {
                    val response = api.searchVacancies(
                        token = token,
                        query = dto.expression,
                        page = dto.page
                    )
                    response.apply { resultCode = SUCCESS }
                } catch (e: IOException) {
                    Log.e("TAG", "Network error", e)
                    createErrorResponse(ERROR_IO_EXCEPTION)
                }
            }
        }
    }

    override suspend fun getIndustries(): List<Industry>? {
        return when {
            !connectManager.isConnected() -> null
            else -> withContext(Dispatchers.IO) {
                try {
                    api.getIndustries(token = token)
                } catch (e: IOException) {
                    Log.e("TAG", "Network error while getting industries", e)
                    null
                }
            }
        }
    }

    override suspend fun getAreas(): List<Area>? {
        return when {
            !connectManager.isConnected() -> null
            else -> withContext(Dispatchers.IO) {
                try {
                    api.getAreas(token = token)
                } catch (e: IOException) {
                    Log.e("TAG", "Network error while getting industries", e)
                    null
                }
            }
        }
    }

    override suspend fun getVacancyDetails(dto: VacancyRequest): Response {
        return when {
            !connectManager.isConnected() -> createErrorResponse(ERROR_NO_CONNECTION)
            else -> withContext(Dispatchers.IO) {
                try {
                    val response = api.getVacancyDetails(
                        token = token,
                        id = dto.id
                    )
                    response.apply { resultCode = SUCCESS }
                } catch (e: IOException) {
                    Log.e("TAG", "Network error", e)
                    createErrorResponse(ERROR_IO_EXCEPTION)
                }
            }
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
