package ru.practicum.android.diploma.ui.screens.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.VacancyInteractor
import ru.practicum.android.diploma.domain.network.models.FilterSettings
import ru.practicum.android.diploma.domain.network.models.VacancyDetails

class MainViewModel(
    private val interactor: VacancyInteractor
) : ViewModel() {

    private val _searchState = MutableLiveData<UiState>()
    val searchState: LiveData<UiState> = _searchState

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    var lastSearchQuery: String? = null
    private var searchJob: Job? = null
    var currentFilterSettings: FilterSettings? = null

    private var currentPage = 0
    private var currentQuery = ""
    private var isLoadingMore = false
    private val allVacancies = mutableListOf<VacancyDetails>()

    fun searchVacancies(query: String, isNewSearch: Boolean = true, filterSettings: FilterSettings? = null) {
        if (query.isNotEmpty()) {
            if (isNewSearch) {
                currentPage = 0
                isLoadingMore = false
                allVacancies.clear()
                currentQuery = query
                currentFilterSettings = filterSettings ?: currentFilterSettings
            }
            val salary = filterSettings?.salary ?: currentFilterSettings?.salary
            val onlyWithSalary = filterSettings?.onlyWithSalary ?: currentFilterSettings?.onlyWithSalary
            val industryId = filterSettings?.selectedIndustry?.id ?: currentFilterSettings?.selectedIndustry?.id
            loadPage(query, currentPage, industryId, salary?.toIntOrNull(), onlyWithSalary)
        }
    }

    private fun loadPage(
        query: String,
        page: Int,
        industryId: String?,
        salary: Int? = null,
        onlyWithSalary: Boolean? = null
    ) {
        searchJob?.cancel()
        _isLoading.postValue(true)
        _searchState.postValue(UiState.Loading)
        viewModelScope.launch {
            interactor.searchVacancy(
                query,
                page,
                industryId,
                salary,
                onlyWithSalary
            ) // Важно: нужен метод с поддержкой страниц!
                .collect { triple ->
                    processResult(
                        vacancies = triple.first,
                        errorMessage = triple.second,
                        vacanciesCount = triple.third,
                        page = page
                    )
                }
        }
    }

    private fun processResult(
        vacancies: List<VacancyDetails>?,
        errorMessage: String?,
        vacanciesCount: String?,
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
                _searchState.postValue(UiState.Content(allVacancies, vacanciesCount!!))
            }
            isLoadingMore = true
            return
        }
        allVacancies.addAll(vacancies)
        _searchState.postValue(UiState.Content(allVacancies, vacanciesCount!!))
        currentPage++
    }

    fun loadMoreItems() {
        if (_isLoading.value == true || isLoadingMore) return
        currentPage++
        val industryId = currentFilterSettings?.selectedIndustry?.id
        val salary = currentFilterSettings?.salary
        val onlyWithSalary = currentFilterSettings?.onlyWithSalary
        loadPage(currentQuery, currentPage, industryId, salary?.toIntOrNull(), onlyWithSalary)
    }

    fun clearSearchResults() {
        _searchState.postValue(UiState.Idle)
    }

}
