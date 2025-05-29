package ru.practicum.android.diploma.domain.network.models

import com.google.gson.annotations.SerializedName
import ru.practicum.android.diploma.data.dto.Response
import ru.practicum.android.diploma.data.dto.VacancyDetailsDto

data class AllVacancyResponse(
    @SerializedName("items") val items: List<VacancyDetailsDto>,
    @SerializedName("page") val page: Int,
    @SerializedName("pages") val pages: Int,
    @SerializedName("per_page") val perPage: Int,
    @SerializedName("found") val found: Int
) : Response()
