package ru.practicum.android.diploma.domain

import ru.practicum.android.diploma.domain.network.models.AllVacancyResponse
import ru.practicum.android.diploma.domain.network.models.VacancyDetails

interface VacancyRepositoryInterface {
    suspend fun getToken()
    suspend fun searchVacancies(query: String, page: Int, perPage: Int = 20): AllVacancyResponse
    suspend fun getVacancyDetails(id: String): VacancyDetails
}
