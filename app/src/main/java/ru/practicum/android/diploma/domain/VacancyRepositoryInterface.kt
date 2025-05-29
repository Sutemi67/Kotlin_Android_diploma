package ru.practicum.android.diploma.domain

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.network.models.AllVacancyResponse
import ru.practicum.android.diploma.domain.network.models.VacancyDetails
import ru.practicum.android.diploma.util.Resource

interface VacancyRepositoryInterface {
    fun searchVacancies(query: String): Flow<Resource<List<VacancyDetails>>>
//    suspend fun getVacancyDetails(id: String): VacancyDetails
}
