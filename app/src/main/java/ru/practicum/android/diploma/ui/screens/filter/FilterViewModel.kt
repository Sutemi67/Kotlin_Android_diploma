package ru.practicum.android.diploma.ui.screens.filter

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.FilterInteractor
import ru.practicum.android.diploma.domain.network.models.Industry
import java.io.IOException

class FilterViewModel(
    private val interactor: FilterInteractor
) : ViewModel() {

    private val _uiState = MutableStateFlow(FilterUiState())
    val uiState: StateFlow<FilterUiState> = _uiState.asStateFlow()

    private var firstList: List<Industry> = emptyList()
    private var filterJob: Job? = null

    init {
        loadIndustries()
    }

    private fun loadIndustries() {
        viewModelScope.launch {
            try {
                val industries = interactor.getIndustries()
                if (industries != null) {
                    firstList = industries
                    _uiState.update {
                        it.copy(
                            industries = industries,
                            isError = false
                        )
                    }
                } else {
                    _uiState.update { it.copy(isError = true) }
                }
            } catch (e: IOException) {
                Log.e("FilterViewModel", "Ошибка при загрузке отраслей", e)
                _uiState.update { it.copy(isError = true) }
            }
        }
    }

    fun onSelectIndustry(industry: Industry?) {
        _uiState.update { it.copy(selectedIndustry = industry) }
    }

    fun setSalary(salaryFilter: String) {
        _uiState.update { it.copy(salary = salaryFilter) }
    }

    fun setOnlyWithSalary(onlyWithSalary: Boolean) {
        _uiState.update { it.copy(onlyWithSalary = onlyWithSalary) }
    }

    fun resetFilters() {
        _uiState.update {
            it.copy(
                selectedIndustry = null,
                workArea = "",
                salary = "",
                onlyWithSalary = false
            )
        }
    }

    fun filterList(text: CharSequence?) {
        filterJob?.cancel()
        filterJob = viewModelScope.launch(Dispatchers.Default) {
            delay(FILTER_DELAY)
            val filteredList = if (text.isNullOrEmpty()) {
                firstList
            } else {
                firstList.filter { it.name.contains(text, ignoreCase = true) }
            }
            _uiState.update { it.copy(industries = filteredList) }
        }
    }

    fun resetList() {
        Log.d("list", "количество элементов сейчас: ${firstList.size}")
        _uiState.update { it.copy(industries = firstList) }
    }

    fun setIndustry(area: String) {
        _uiState.update { it.copy(workArea = area) }
        Log.d("area", "текст установлен на: $area")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("area", "ViewModel удалена")
    }

    companion object {
        private const val FILTER_DELAY = 500L
    }
}
