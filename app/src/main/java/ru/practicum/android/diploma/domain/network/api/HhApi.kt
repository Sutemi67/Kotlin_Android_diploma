package ru.practicum.android.diploma.domain.network.api

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import ru.practicum.android.diploma.domain.network.models.AllVacancyResponse
import ru.practicum.android.diploma.domain.network.models.TokenResponse
import ru.practicum.android.diploma.domain.network.models.VacancyDetails

interface HhApi {
    @POST("/token")
    suspend fun getToken(
        @Query("client_id") client: String,
        @Query("client_secret") secret: String,
        @Query("grant_type") type: String
    ): TokenResponse

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
    ): VacancyDetails
}
