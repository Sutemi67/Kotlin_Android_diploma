package ru.practicum.android.diploma.data.dto

import com.google.gson.annotations.SerializedName
import ru.practicum.android.diploma.domain.network.models.Area
import ru.practicum.android.diploma.domain.network.models.Employer
import ru.practicum.android.diploma.domain.network.models.Employment
import ru.practicum.android.diploma.domain.network.models.Experience
import ru.practicum.android.diploma.domain.network.models.Salary
import ru.practicum.android.diploma.domain.network.models.Schedule

data class VacancyDetailsDto(
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
