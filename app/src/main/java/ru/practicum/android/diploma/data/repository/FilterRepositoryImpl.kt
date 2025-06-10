package ru.practicum.android.diploma.data.repository

import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.domain.FilterRepository
import ru.practicum.android.diploma.domain.network.models.Area
import ru.practicum.android.diploma.domain.network.models.Industry

class FilterRepositoryImpl(
    private val networkClient: NetworkClient,
) : FilterRepository {

    override suspend fun getIndustries(): List<Industry>? {
        return networkClient.getIndustries()
    }

    override suspend fun getAreas(): List<Area>? {
        return networkClient.getAreas()
    }

}
