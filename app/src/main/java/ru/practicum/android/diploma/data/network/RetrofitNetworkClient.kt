package ru.practicum.android.diploma.data.network

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.data.dto.Response
import ru.practicum.android.diploma.domain.network.api.HhApi
import ru.practicum.android.diploma.domain.network.models.AllVacancyRequest
import ru.practicum.android.diploma.util.isConnected

class RetrofitNetworkClient(
    private val context: Context,
    private val api: HhApi
) : NetworkClient {

    private val token = "Bearer ${BuildConfig.HH_ACCESS_TOKEN}"
    private val token1 = "Bearer APPLIPKQ37D33OTAKDG005QH3FHC9UMI2MA1VMHIH8IHGL5THOG4P5AQJH8MCULD"

    override suspend fun doSearchRequest(dto: Any): Response {
        if(!isConnected(context)) {
            return Response().apply { resultCode = -1 }
        }
        if (dto !is AllVacancyRequest) {
            return Response().apply { resultCode = 400 }
        }

        return withContext(Dispatchers.IO) {
            try {
                val resp = api.searchVacancies(
                    token = token1,
                    query = dto.expression,
                    page = 1
                )
                resp.apply { resultCode = 200 }
            } catch (e: Throwable) {
                Response().apply { resultCode = 500 }
            }
        }
    }

}
