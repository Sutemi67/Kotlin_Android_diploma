package ru.practicum.android.diploma.ui.screens.favourites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.VacancyInteractorInterface

class FavouritesViewModel(
    private val interactor: VacancyInteractorInterface
) : ViewModel() {

    private val _favouriteState = MutableLiveData<UiStateFavourites>()
    val favouriteState: LiveData<UiStateFavourites> = _favouriteState

    fun getFavoriteVacancy() {
        viewModelScope.launch {
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
