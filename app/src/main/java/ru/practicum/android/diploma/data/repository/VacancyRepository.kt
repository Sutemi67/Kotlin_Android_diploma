package ru.practicum.android.diploma.data.repository

import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.domain.VacancyRepositoryInterface
import ru.practicum.android.diploma.domain.network.api.HhApi
import ru.practicum.android.diploma.domain.network.models.AllVacancyResponse
import ru.practicum.android.diploma.domain.network.models.VacancyDetails

class VacancyRepository(
    private val api: HhApi
) : VacancyRepositoryInterface {
    private val token = "Bearer ${BuildConfig.HH_ACCESS_TOKEN}"

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
