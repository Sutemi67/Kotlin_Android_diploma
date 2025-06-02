package ru.practicum.android.diploma.domain

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.network.models.VacancyDetails

interface VacancyInteractorInterface {
    fun searchVacancy(expression: String, page: Int): Flow<Triple<List<VacancyDetails>?, String?, String?>>
    suspend fun getVacancyDetails(id: String): Flow<Pair<VacancyDetails?, String?>>
}
