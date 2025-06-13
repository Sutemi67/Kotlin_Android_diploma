package ru.practicum.android.diploma.ui.screens.favourites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.VacancyInteractor

class FavouritesViewModel(
    private val interactor: VacancyInteractor
) : ViewModel() {

    private val _favouriteState = MutableLiveData<UiStateFavourites>()
    val favouriteState: LiveData<UiStateFavourites> = _favouriteState
    private var getFavoriteVacanciesJob: Job? = null

    fun getFavoriteVacancy() {
        getFavoriteVacanciesJob?.cancel()
        getFavoriteVacanciesJob = viewModelScope.launch {
            interactor
                .getAllFavoriteVacancy()
                .collect {
                    _favouriteState.value = if (it.isEmpty()) {
                        UiStateFavourites.Empty
                    } else {
                        UiStateFavourites.Content(it)
                    }
                }
        }
    }

}
