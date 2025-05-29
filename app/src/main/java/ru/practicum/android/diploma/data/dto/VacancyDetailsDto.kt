package ru.practicum.android.diploma.data.dto

import com.google.gson.annotations.SerializedName
import ru.practicum.android.diploma.domain.network.models.Area
import ru.practicum.android.diploma.domain.network.models.Employer
import ru.practicum.android.diploma.domain.network.models.Salary
import ru.practicum.android.diploma.domain.network.models.Snippet

data class VacancyDetailsDto(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("area") val area: Area,
    @SerializedName("salary") val salary: Salary?,
    @SerializedName("employer") val employer: Employer,
    @SerializedName("snippet") val snippet: Snippet,
    @SerializedName("alternate_url") val alternateUrl: String,
)
