package ru.practicum.android.diploma.data.dto

data class AllVacancyRequest(
    val expression: String,
    val page: Int,
    val industry: String? = null,
    val salary: Int? = null,
    val onlyWithSalary: Boolean? = false
)
