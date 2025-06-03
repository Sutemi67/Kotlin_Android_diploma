package ru.practicum.android.diploma.ui.screens.vacancy

import android.webkit.WebViewClient.ERROR_CONNECT
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.VacancyInteractorInterface
import ru.practicum.android.diploma.domain.network.models.VacancyDetails
import ru.practicum.android.diploma.data.db.entity.VacancyEntity
import ru.practicum.android.diploma.util.Resource

class VacancyDetailsViewModel(
    private val interactor: VacancyInteractorInterface
) : ViewModel() {

    private val _vacancyDetails = MutableLiveData<UiStateVacancy>()
    val vacancyDetails: LiveData<UiStateVacancy> = _vacancyDetails

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> = _isFavorite

    fun getVacancyDetails(id: String) {
        if (id.isNotEmpty()) {
            renderState(UiStateVacancy.Loading)
            viewModelScope.launch {
                interactor.getVacancyDetails(id).collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let { renderState(UiStateVacancy.Content(it)) }
                        }
                        is Resource.Error -> {
                            if (result.message == "$ERROR_CONNECT") {
                                renderState(UiStateVacancy.Error(result.message))
                            } else {
                                renderState(UiStateVacancy.ErrorService)
                            }
                        }
                    }
                }
            }
            checkFavoriteStatus(id.toInt())
        }
    }

    private fun checkFavoriteStatus(vacancyId: Int) {
        viewModelScope.launch {
            val vacancy = interactor.getFavoriteVacancy(vacancyId)
            _isFavorite.postValue(vacancy != null)
        }
    }

    fun toggleFavorite(vacancy: VacancyDetails) {
        viewModelScope.launch {
            val vacancyId = vacancy.id.toInt()
            val isCurrentlyFavorite = _isFavorite.value == true
            
            if (isCurrentlyFavorite) {
                interactor.removeFromFavorites(vacancyId)
                _isFavorite.postValue(false)
            } else {
                val vacancyEntity = VacancyEntity(
                    id = vacancyId,
                    name = vacancy.name,
                    area = vacancy.area.name,
                    salary = vacancy.salary?.let {
                        when {
                            it.from != null && it.to != null -> "${it.from} - ${it.to} ${it.currency}"
                            it.from != null -> "от ${it.from} ${it.currency}"
                            it.to != null -> "до ${it.to} ${it.currency}"
                            else -> "Не указана"
                        }
                    },
                    employer = vacancy.employer.name,
                    snippet = vacancy.snippet?.responsibility ?: "",
                    alternateUrl = vacancy.alternateUrl
                )
                interactor.addToFavorites(vacancyEntity)
                _isFavorite.postValue(true)
            }
        }
    }

    private fun renderState(state: UiStateVacancy) {
        _vacancyDetails.postValue(state)
    }
}
