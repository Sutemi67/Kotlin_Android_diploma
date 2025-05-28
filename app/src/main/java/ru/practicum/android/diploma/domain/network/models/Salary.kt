package ru.practicum.android.diploma.domain.network.models

import com.google.gson.annotations.SerializedName

data class Salary(
    @SerializedName("from") val from: Int?,
    @SerializedName("to") val to: Int?,
    @SerializedName("currency") val currency: String?
)
