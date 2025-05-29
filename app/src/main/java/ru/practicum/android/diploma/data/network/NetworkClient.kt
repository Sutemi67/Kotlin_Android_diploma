package ru.practicum.android.diploma.data.network

import ru.practicum.android.diploma.data.dto.Response

interface NetworkClient {

    suspend fun doSearchRequest(dto: Any): Response

}
