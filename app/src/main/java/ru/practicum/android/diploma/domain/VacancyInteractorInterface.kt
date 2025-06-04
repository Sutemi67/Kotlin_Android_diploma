package ru.practicum.android.diploma.domain

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.db.entity.VacancyEntity
import ru.practicum.android.diploma.domain.network.models.VacancyDetails
import ru.practicum.android.diploma.util.Resource

interface VacancyInteractorInterface {
    fun searchVacancy(query: String, page: Int): Flow<Triple<List<VacancyDetails>?, String?, String?>>
    suspend fun getVacancyDetails(id: String): Flow<Resource<VacancyDetails>>
    suspend fun addToFavorites(vacancy: VacancyEntity)
    suspend fun removeFromFavorites(vacancyId: Int)
    suspend fun getFavoriteVacancy(vacancyId: Int): VacancyEntity?
    fun shareVacancy(url: String)
    fun getAllFavoriteVacancy(): Flow<List<VacancyDetails>>
}
