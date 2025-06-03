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
import ru.practicum.android.diploma.data.network.ConnectManager
import ru.practicum.android.diploma.data.converters.VacancyDbConvertor

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
        if (id.isNotEmpty()) {
            renderState(UiStateVacancy.Loading)
            viewModelScope.launch {
                if (connectManager.isConnected()) {
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
                } else {
                    // Если нет интернета, пробуем загрузить из базы данных
                    val vacancyEntity = interactor.getFavoriteVacancy(id.toInt())
                    if (vacancyEntity != null) {
                        val vacancyDetails = converter.mapToDomain(vacancyEntity)
                        renderState(UiStateVacancy.Content(vacancyDetails))
                    } else {
                        renderState(UiStateVacancy.Error("Нет подключения к интернету и вакансия не найдена в избранном"))
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
                val vacancyEntity = converter.mapToEntity(vacancy)
                interactor.addToFavorites(vacancyEntity)
                _isFavorite.postValue(true)
            }
        }
    }

    private fun renderState(state: UiStateVacancy) {
        _vacancyDetails.postValue(state)
    }
}
