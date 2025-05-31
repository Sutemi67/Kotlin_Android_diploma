package ru.practicum.android.diploma.domain.interactor

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.domain.VacancyInteractorInterface
import ru.practicum.android.diploma.domain.VacancyRepositoryInterface
import ru.practicum.android.diploma.domain.network.models.VacancyDetails
import ru.practicum.android.diploma.util.Resource

class VacancyInteractorImpl(private val repository: VacancyRepositoryInterface) : VacancyInteractorInterface {

    override fun searchVacancy(query: String, page: Int): Flow<Triple<List<VacancyDetails>?, String?, String?>> {
        return repository.searchVacancies(query).map { result ->
            when (result) {
                is Resource.Success -> {
                    Triple(result.data, null, result.itemsCount.toString())

                }

                is Resource.Error -> {
                    Triple(null, result.message, null)
                }
            }
        }
    }

}
