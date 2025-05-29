package ru.practicum.android.diploma.ui.screens.main

import ru.practicum.android.diploma.domain.network.models.VacancyDetails

sealed class UiState {

    object Idle : UiState()

    object Loading : UiState()

    data class Content(
        val vacancies: List<VacancyDetails>,
    ) : UiState()

    object NotFound : UiState()

    data class Error(
        val errorMessage: String
    ) : UiState()

}
