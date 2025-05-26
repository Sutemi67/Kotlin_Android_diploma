package ru.practicum.android.diploma.domain.network.models

import com.google.gson.annotations.SerializedName

data class AllVacancyResponse(
    @SerializedName("items") val items: List<VacancyDetails>,
    @SerializedName("page") val page: Int,
    @SerializedName("pages") val pages: Int,
    @SerializedName("per_page") val perPage: Int,
    @SerializedName("found") val found: Int
)
