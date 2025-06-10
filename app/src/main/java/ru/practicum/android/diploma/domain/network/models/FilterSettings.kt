package ru.practicum.android.diploma.domain.network.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FilterSettings(
    val selectedIndustry: Industry? = null,
    val salary: String? = null,
    val onlyWithSalary: Boolean? = false
) : Parcelable
