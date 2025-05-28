package ru.practicum.android.diploma.ui.screens.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import ru.practicum.android.diploma.domain.VacancyRepositoryInterface
import ru.practicum.android.diploma.domain.network.models.VacancyDetails
import java.io.IOException

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

//                val currentList = _vacancies.value?.toMutableList() ?: mutableListOf()
//                currentList.addAll(response.items)
                _vacancies.value = response.items
//                _vacancies.value = currentList

                currentPage++
            } catch (e: IOException) {
                _error.value = e.message
            } catch (e: HttpException) {
                _error.value = when (e.code()) {
                    HTTP_FORBIDDEN -> "Ошибка авторизации. Проверьте токен доступа"
                    HTTP_NOT_FOUND -> "Вакансии не найдены"
                    HTTP_SERVER_ERROR -> "Ошибка сервера"
                    else -> "Ошибка сервера: ${e.code()}"
                }
            } finally {
                _isLoading.value = false
                isLoadingMore = false
            }
        }
    }

    fun loadMoreVacancies() {
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
