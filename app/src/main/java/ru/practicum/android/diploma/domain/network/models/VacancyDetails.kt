package ru.practicum.android.diploma.domain.network.models

import com.google.gson.annotations.SerializedName

data class VacancyDetails(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("area") val area: Area,
    @SerializedName("salary") val salary: Salary?,
    @SerializedName("employer") val employer: Employer,
    @SerializedName("snippet") val snippet: Snippet,
    @SerializedName("alternate_url") val alternateUrl: String
)
