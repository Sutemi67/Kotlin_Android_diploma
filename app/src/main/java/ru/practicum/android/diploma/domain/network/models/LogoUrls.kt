package ru.practicum.android.diploma.domain.network.models

import com.google.gson.annotations.SerializedName

data class LogoUrls(
    @SerializedName("original") val original: String?,
)
