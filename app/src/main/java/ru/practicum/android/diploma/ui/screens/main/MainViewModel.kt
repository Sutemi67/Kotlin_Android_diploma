package ru.practicum.android.diploma.ui.screens.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import ru.practicum.android.diploma.domain.VacancyRepositoryInterface
import ru.practicum.android.diploma.domain.network.models.VacancyDetails
import ru.practicum.android.diploma.util.Resource

class MainViewModel(
    private val repository: VacancyRepositoryInterface
) : ViewModel() {

    private val _searchState = MutableLiveData<Resource<List<VacancyDetails>>>()
    val searchState: LiveData<Resource<List<VacancyDetails>>> = _searchState

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var currentPage = 0
    private var totalPages = 0
    private var totalVacancies = 0
    private var currentQuery = ""
    private var isLoadingMore = false
    private var currentItems = mutableListOf<VacancyDetails>()

    fun searchVacancies(query: String, isNewSearch: Boolean = true) {
        if (isNewSearch) {
            currentPage = 0
            currentQuery = query
            currentItems.clear()
            _searchState.value = Resource.Success(emptyList(), totalVacancies)
        }

        if (isLoadingMore || currentPage >= totalPages && currentPage > 0) return

        viewModelScope.launch {
            try {
                isLoadingMore = true
                _isLoading.value = true

                val response = repository.searchVacancies(currentQuery, currentPage)
                totalPages = response.pages

                if (response.items.isEmpty() && currentPage == 0) {
                    _searchState.value = Resource.Error("По вашему запросу ничего не найдено")
                } else {
                    currentItems.addAll(response.items)
                    totalVacancies = currentItems.size
                    _searchState.value = Resource.Success(currentItems.toList(), totalVacancies)
                }

                currentPage++
            } catch (e: HttpException) {
                _searchState.value = Resource.Error(
                    when (e.code()) {
                        HTTP_FORBIDDEN -> "Ошибка авторизации. Проверьте токен доступа"
                        HTTP_NOT_FOUND -> "Вакансии не найдены"
                        HTTP_SERVER_ERROR -> "Ошибка сервера"
                        else -> "Ошибка сервера: ${e.code()}"
                    }
                )
            } finally {
                isLoadingMore = false
                _isLoading.value = false
            }
        }
    }

    fun loadMoreItems() {
        if (!isLoadingMore && currentPage < totalPages) {
            searchVacancies(currentQuery, false)
        }
    }

    companion object {
        private const val HTTP_FORBIDDEN = 403
        private const val HTTP_NOT_FOUND = 404
        private const val HTTP_SERVER_ERROR = 500
    }
}
