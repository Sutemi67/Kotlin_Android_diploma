package ru.practicum.android.diploma.domain.network.models

import com.google.gson.annotations.SerializedName

data class Industry(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("parent_id") val parentId: String?,
    @SerializedName("industries") val industries: List<Industry>?
) 