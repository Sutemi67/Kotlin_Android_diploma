package ru.practicum.android.diploma.ui.screens.vacancy

import android.webkit.WebViewClient.ERROR_CONNECT
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.data.converters.VacancyDbConvertor
import ru.practicum.android.diploma.data.network.ConnectManager
import ru.practicum.android.diploma.domain.VacancyInteractorInterface
import ru.practicum.android.diploma.domain.network.models.VacancyDetails
import ru.practicum.android.diploma.util.Resource

class VacancyDetailsViewModel(
    private val interactor: VacancyInteractorInterface,
    private val connectManager: ConnectManager,
    private val converter: VacancyDbConvertor
) : ViewModel() {

    private val _vacancyDetails = MutableLiveData<UiStateVacancy>()
    val vacancyDetails: LiveData<UiStateVacancy> = _vacancyDetails

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> = _isFavorite

    fun getVacancyDetails(id: String) {
        if (id.isEmpty()) return

        renderState(UiStateVacancy.Loading)
        viewModelScope.launch {
            if (connectManager.isConnected()) {
                loadFromNetwork(id)
            } else {
                loadFromDatabase(id)
            }
            checkFavoriteStatus(id.toInt())
        }
    }

    private suspend fun loadFromNetwork(id: String) {
        interactor.getVacancyDetails(id).collect { result ->
            when (result) {
                is Resource.Success -> handleSuccess(result)
                is Resource.Error -> handleError(result)
            }
        }
    }

    private fun handleSuccess(result: Resource.Success<VacancyDetails>) {
        result.data?.let { renderState(UiStateVacancy.Content(it)) }
    }

    private fun handleError(result: Resource.Error<VacancyDetails>) {
        val state = if (result.message == "$ERROR_CONNECT") {
            UiStateVacancy.Error(result.message)
        } else {
            UiStateVacancy.ErrorService
        }
        renderState(state)
    }

    private suspend fun loadFromDatabase(id: String) {
        val vacancyEntity = interactor.getFavoriteVacancy(id.toInt())
        val state = if (vacancyEntity != null) {
            val vacancyDetails = converter.mapToDomain(vacancyEntity)
            UiStateVacancy.Content(vacancyDetails)
        } else {
            UiStateVacancy.Error("Нет подключения к интернету и вакансия не найдена в избранном")
        }
        renderState(state)
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
                val vacancyEntity = converter.mapToEntity(vacancy)
                interactor.addToFavorites(vacancyEntity)
                _isFavorite.postValue(true)
            }
        }
    }

    fun shareVacancy(id: String) {
        interactor.shareVacancy(BASE_URL + id)
    }

    private fun renderState(state: UiStateVacancy) {
        _vacancyDetails.postValue(state)
    }

    companion object {
        const val BASE_URL = "https://hh.ru/vacancy/"
    }
}
