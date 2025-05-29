package ru.practicum.android.diploma.data.network

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.data.dto.Response
import ru.practicum.android.diploma.domain.network.api.HhApi
import ru.practicum.android.diploma.domain.network.models.AllVacancyRequest
import ru.practicum.android.diploma.util.isConnected
import java.io.IOException

class RetrofitNetworkClient(
    private val context: Context,
    private val api: HhApi
) : NetworkClient {

    private val token = "Bearer ${BuildConfig.HH_ACCESS_TOKEN}"

    override suspend fun doSearchRequest(dto: Any): Response {
        val result = when {
            !isConnected(context) -> {
                Response().apply { resultCode = ERROR_NO_CONNECTION }
            }
            dto !is AllVacancyRequest -> {
                Response().apply { resultCode = ERROR_INVALID_DTO }
            }
            else -> withContext(Dispatchers.IO) {
                try {
                    val resp = api.searchVacancies(
                        token = token,
                        query = dto.expression,
                        page = 1
                    )
                    resp.apply { resultCode = SUCCESS }
                } catch (e: IOException) {
                    Log.e("TAG", "Network error", e)
                    Response().apply { resultCode = ERROR_IO_EXCEPTION }
                }
            }
        }
        return result
    }

    companion object {
        private const val ERROR_NO_CONNECTION = -1
        private const val ERROR_INVALID_DTO = 400
        private const val ERROR_IO_EXCEPTION = 500
        private const val SUCCESS = 200
    }

}
