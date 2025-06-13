package ru.practicum.android.diploma.domain

import ru.practicum.android.diploma.domain.network.models.Area
import ru.practicum.android.diploma.domain.network.models.Industry

interface FilterRepository {
    suspend fun getIndustries(): List<Industry>?
    suspend fun getAreas(): List<Area>?

}
