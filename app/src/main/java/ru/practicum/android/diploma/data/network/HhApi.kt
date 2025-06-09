package ru.practicum.android.diploma.data.network

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
import ru.practicum.android.diploma.data.dto.VacancyResponse
import ru.practicum.android.diploma.data.dto.AllVacancyResponse
import ru.practicum.android.diploma.domain.network.models.Industry

interface HhApi {
    @GET("vacancies")
    suspend fun searchVacancies(
        @Header("Authorization") token: String,
        @Query("text") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int = 20
    ): AllVacancyResponse

    @GET("vacancies/{id}")
    suspend fun getVacancyDetails(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): VacancyResponse

    @GET("industries")
    suspend fun getIndustries(
        @Header("Authorization") token: String
    ): List<Industry>
}
