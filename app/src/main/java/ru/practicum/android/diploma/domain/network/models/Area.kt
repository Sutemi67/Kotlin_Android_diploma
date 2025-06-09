package ru.practicum.android.diploma.domain.network.models

import com.google.gson.annotations.SerializedName

data class Area(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("area") val areas: List<Area>
)
