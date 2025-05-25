package ru.practicum.android.diploma.domain.network.models

import com.google.gson.annotations.SerializedName

data class TokenResponse(
    @SerializedName("access_token") val token: String,
    @SerializedName("token_type") val type: String,
)
