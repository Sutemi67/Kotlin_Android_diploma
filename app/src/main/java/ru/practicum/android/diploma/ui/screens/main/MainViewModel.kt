package ru.practicum.android.diploma.ui.screens.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.VacancyInteractorInterface
import ru.practicum.android.diploma.domain.network.models.VacancyDetails

class MainViewModel(
    private val interactor: VacancyInteractorInterface
) : ViewModel() {

    private val _searchState = MutableLiveData<UiState>()
    val searchState: LiveData<UiState> = _searchState

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var currentPage = 0
    private var totalPages = 0
    private var totalVacancies = 0
    private var currentQuery = ""
    private var isLoadingMore = false

    fun searchVacancies(query: String, isNewSearch: Boolean = true) {
        if(query.isNotEmpty()) {

            _searchState.postValue(UiState.Loading)

            viewModelScope.launch {
                interactor
                    .searchVacancy(query)
                    .collect { pair ->
                        processResult(pair.first, pair.second)
                    }
            }

        }
    }

    private fun processResult(vacancy: List<VacancyDetails>?, errorMessage: String?) {
        val vacancys = mutableListOf<VacancyDetails>()
        if(vacancy != null) {
            vacancys.addAll(vacancy)
        }

        when {
            errorMessage != null -> _searchState.postValue(UiState.Error(errorMessage))

            vacancys.isEmpty() -> _searchState.postValue(UiState.NotFound)

            else -> _searchState.postValue(UiState.Content(vacancys))
        }

    }

    fun loadMoreItems() {
        if (!isLoadingMore && currentPage < totalPages) {
            searchVacancies(currentQuery, false)
        }
    }

}
