package ru.practicum.android.diploma.ui.screens.vacancy

import ru.practicum.android.diploma.domain.network.models.VacancyDetails

sealed class UiStateVacancy {
    object Loading : UiStateVacancy()
    data class Content(val vacancy: VacancyDetails) : UiStateVacancy()
    data class Error(val message: String) : UiStateVacancy()
    object ErrorService : UiStateVacancy()
}



