package ru.practicum.android.diploma.ui.screens.vacancy

import android.webkit.WebViewClient.ERROR_CONNECT
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.VacancyInteractorInterface
import ru.practicum.android.diploma.domain.network.models.VacancyDetails

class VacancyDetailsViewModel(
    private val interactor: VacancyInteractorInterface
) : ViewModel() {

    private val _vacancyDetails = MutableLiveData<UiStateVacancy>()
    val vacancyDetails: LiveData<UiStateVacancy> = _vacancyDetails

    fun getVacancyDetails(id: String) {
        if (id.isNotEmpty()) {
            renderState(UiStateVacancy.Loading)
            viewModelScope.launch {
                interactor
                    .getVacancyDetails(id)
                    .collect { pair ->
                        processResult(pair.first, pair.second)
                    }
            }
        }
    }

    private fun processResult(vacancy: VacancyDetails?, errorMessage: String?) {

        when {
            errorMessage != null -> {

                if (errorMessage == "$ERROR_CONNECT") {
                    renderState(UiStateVacancy.Error(errorMessage))
                } else {
                    renderState(UiStateVacancy.ErrorService)
                }
            }

            /*    vacancy. .isEmpty() -> {
                    renderState(UiStateVacancy.Empty)
                }*/

            else -> {
                vacancy?.let { renderState(UiStateVacancy.Content(it)) }
            }
        }
    }

    private fun renderState(state: UiStateVacancy) {
        _vacancyDetails.postValue(state)
    }
}
