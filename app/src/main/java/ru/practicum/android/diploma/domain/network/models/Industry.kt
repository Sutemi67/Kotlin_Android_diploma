package ru.practicum.android.diploma.domain.network.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Industry(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("parent_id") val parentId: String?,
    @SerializedName("industries") val industries: List<Industry>?
) : Parcelable
