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
    private var currentQuery = ""
    private var isLoadingMore = false
    private val allVacancies = mutableListOf<VacancyDetails>()

    fun searchVacancies(query: String, isNewSearch: Boolean = true) {
        if (query.isNotEmpty()) {
            if (isNewSearch) {
                currentPage = 0
                isLoadingMore = false
                allVacancies.clear()
                currentQuery = query
            }
            loadPage(query, currentPage)
        }
    }

    private fun loadPage(query: String, page: Int) {
        _isLoading.postValue(true)
        _searchState.postValue(UiState.Loading)
        viewModelScope.launch {
            interactor
                .searchVacancy(query, page) // Важно: нужен метод с поддержкой страниц!
                .collect { pair ->
                    processResult(pair.first, pair.second, page)
                }
        }
    }

    private fun processResult(
        vacancies: List<VacancyDetails>?,
        errorMessage: String?,
        page: Int
    ) {
        _isLoading.postValue(false)

        if (errorMessage != null) {
            _searchState.postValue(UiState.Error(errorMessage))
            return
        }

        if (vacancies.isNullOrEmpty()) {
            if (page == 0) {
                _searchState.postValue(UiState.NotFound)
            } else {
                _searchState.postValue(UiState.Content(allVacancies))
            }
            isLoadingMore = true
            return
        }
        allVacancies.addAll(vacancies)
        _searchState.postValue(UiState.Content(allVacancies))
        currentPage++ // увеличиваем страницу
    }


    fun loadMoreItems() {
        if (_isLoading.value == true || isLoadingMore) return
        currentPage++
        loadPage(currentQuery, currentPage)
    }

}
