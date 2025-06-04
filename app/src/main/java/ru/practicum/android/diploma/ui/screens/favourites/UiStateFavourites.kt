package ru.practicum.android.diploma.ui.screens.favourites

import ru.practicum.android.diploma.domain.network.models.VacancyDetails

sealed class UiStateFavourites {
    class Content(val vacancy: List<VacancyDetails>) : UiStateFavourites()
    object Empty : UiStateFavourites()
    object Error : UiStateFavourites()
}
