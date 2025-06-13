package ru.practicum.android.diploma.data.network

import ru.practicum.android.diploma.data.dto.AllVacancyRequest
import ru.practicum.android.diploma.data.dto.Response
import ru.practicum.android.diploma.data.dto.VacancyRequest
import ru.practicum.android.diploma.domain.network.models.Area
import ru.practicum.android.diploma.domain.network.models.Industry

interface NetworkClient {

    suspend fun doSearchRequest(dto: AllVacancyRequest): Response
    suspend fun getVacancyDetails(dto: VacancyRequest): Response
    suspend fun getIndustries(): List<Industry>?
    suspend fun getAreas(): List<Area>?
}
