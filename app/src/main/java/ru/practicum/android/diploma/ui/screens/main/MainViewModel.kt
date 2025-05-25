package ru.practicum.android.diploma.ui.screens.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.VacancyRepositoryInterface
import ru.practicum.android.diploma.domain.network.models.VacancyDetails

class MainViewModel(
    private val repository: VacancyRepositoryInterface
) : ViewModel() {

    private val _vacancies = MutableLiveData<List<VacancyDetails>>()
    val vacancies: LiveData<List<VacancyDetails>> = _vacancies

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private var currentPage = 0
    private var totalPages = 0
    private var currentQuery = ""
    private var isLoadingMore = false

    fun searchVacancies(query: String, isNewSearch: Boolean = true) {
        if (isNewSearch) {
            currentPage = 0
            currentQuery = query
            _vacancies.value = emptyList()
        }

        if (isLoadingMore || currentPage >= totalPages && currentPage > 0) return

        viewModelScope.launch {
            try {
                isLoadingMore = true
                _isLoading.value = true
                _error.value = null

                val response = repository.searchVacancies(currentQuery, currentPage)
                totalPages = response.pages

                val currentList = _vacancies.value?.toMutableList() ?: mutableListOf()
                currentList.addAll(response.items)
                _vacancies.value = currentList

                currentPage++
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
                isLoadingMore = false
            }
        }
    }

    fun getToken() = viewModelScope.launch { repository.getToken() }

    fun loadMoreVacancies() {
        if (!isLoadingMore && currentPage < totalPages) {
            searchVacancies(currentQuery, false)
        }
    }
}
