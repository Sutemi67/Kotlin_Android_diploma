package ru.practicum.android.diploma.domain.interactor

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.db.entity.VacancyEntity
import ru.practicum.android.diploma.domain.VacancyInteractorInterface
import ru.practicum.android.diploma.domain.VacancyRepositoryInterface
import ru.practicum.android.diploma.domain.network.models.VacancyDetails
import ru.practicum.android.diploma.sharing.domain.ExternalNavigator
import ru.practicum.android.diploma.util.Resource

class VacancyInteractorImpl(
    private val repository: VacancyRepositoryInterface,
    private val externalNavigator: ExternalNavigator
) : VacancyInteractorInterface {

    override fun searchVacancy(query: String, page: Int): Flow<Triple<List<VacancyDetails>?, String?, String?>> {
        return repository.searchVacancy(query, page)
    }

    override suspend fun getVacancyDetails(id: String): Flow<Resource<VacancyDetails>> {
        return repository.getVacancyDetails(id)
    }

    override suspend fun addToFavorites(vacancy: VacancyEntity) {
        repository.addToFavorites(vacancy)
    }

    override suspend fun removeFromFavorites(vacancyId: Int) {
        repository.removeFromFavorites(vacancyId)
    }

    override suspend fun getFavoriteVacancy(vacancyId: Int): VacancyEntity? {
        return repository.getFavoriteVacancy(vacancyId)
    }
    override fun shareVacancy(url: String) {
        externalNavigator.shareVacancy(url)
    }
}
