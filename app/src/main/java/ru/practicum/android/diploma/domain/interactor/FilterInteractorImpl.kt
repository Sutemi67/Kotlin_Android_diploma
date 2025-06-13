package ru.practicum.android.diploma.domain.interactor

import ru.practicum.android.diploma.domain.FilterInteractor
import ru.practicum.android.diploma.domain.FilterRepository
import ru.practicum.android.diploma.domain.network.models.Area
import ru.practicum.android.diploma.domain.network.models.Industry

class FilterInteractorImpl(private val repository: FilterRepository) : FilterInteractor {

    override suspend fun getIndustries(): List<Industry>? {
        return repository.getIndustries()
    }

    override suspend fun getAreas(): List<Area>? {
        return repository.getAreas()
    }

}
