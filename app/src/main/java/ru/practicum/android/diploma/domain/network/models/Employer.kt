package ru.practicum.android.diploma.domain.network.models

import com.google.gson.annotations.SerializedName

data class Employer(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("logo_url") val logoUrl: String?
)
