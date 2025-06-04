package ru.practicum.android.diploma.domain.network.models

import com.google.gson.annotations.SerializedName

data class VacancyDetails(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("area") val area: Area,
    @SerializedName("salary") val salary: Salary?,
    @SerializedName("employer") val employer: Employer,
    @SerializedName("experience") val experience: Experience,
    @SerializedName("alternate_url") val alternateUrl: String,
    @SerializedName("schedule") val schedule: Schedule,
    @SerializedName("employment") val employment: Employment,
    @SerializedName("description") val description: String?,
)

