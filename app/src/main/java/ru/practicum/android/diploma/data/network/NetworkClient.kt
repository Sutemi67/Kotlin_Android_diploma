package ru.practicum.android.diploma.data.network

import ru.practicum.android.diploma.data.dto.AllVacancyRequest
import ru.practicum.android.diploma.data.dto.Response
import ru.practicum.android.diploma.data.dto.VacancyRequest

interface NetworkClient {

    suspend fun doSearchRequest(dto: AllVacancyRequest): Response
    suspend fun getVacancyDetails(dto: VacancyRequest): Response

}
