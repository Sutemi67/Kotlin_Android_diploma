package ru.practicum.android.diploma.data.repository

import android.util.Log
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.domain.VacancyRepositoryInterface
import ru.practicum.android.diploma.domain.network.api.HhApi
import ru.practicum.android.diploma.domain.network.models.AllVacancyResponse
import ru.practicum.android.diploma.domain.network.models.VacancyDetails

class VacancyRepository(
    private val api: HhApi
) : VacancyRepositoryInterface {

    private val token = "Bearer ${BuildConfig.HH_ACCESS_TOKEN}"

    override suspend fun getToken() {
        val response = api.getToken(
            "H9LEG025S5TH6BUPPDV6E2PJFVTQ6MVR0JQJ0U8HO15D02R98AJ3MG5MJSV0PI80",
            "QP8377SKTFH21CPBV5HG8O4AQT1IKGT4S23OCRV61HJG4Q8FOS083CU9P0EAC3TK",
            "client_credentials"
        )
        Log.d("token", response.toString())
        Log.e("token", response.token)
    }

    override suspend fun searchVacancies(
        query: String,
        page: Int,
        perPage: Int
    ): AllVacancyResponse {
        return api.searchVacancies(token, query, page, perPage)
    }

    override suspend fun getVacancyDetails(
        id: String
    ): VacancyDetails {
        return api.getVacancyDetails(token, id)
    }
}
