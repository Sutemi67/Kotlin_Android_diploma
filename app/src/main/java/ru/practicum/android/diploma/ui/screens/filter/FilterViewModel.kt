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
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.domain.FilterInteractor
import ru.practicum.android.diploma.domain.network.models.Industry
import java.io.IOException

class FilterViewModel(
    private val interactor: FilterInteractor
) : ViewModel() {
    private var _workArea = MutableStateFlow("Отрасль")
    val workArea: StateFlow<String> = _workArea.asStateFlow()

    private var _industries = MutableStateFlow<List<Industry>>(emptyList())
    val industries: StateFlow<List<Industry>> = _industries.asStateFlow()

    private val _selectedIndustry = MutableStateFlow<Industry?>(null)
    val selectedIndustry: StateFlow<Industry?> = _selectedIndustry.asStateFlow()

    private val _salary = MutableStateFlow("")
    val salary: StateFlow<String> = _salary.asStateFlow()

    private val _onlyWithSalary = MutableStateFlow(false)
    val onlyWithSalary: StateFlow<Boolean> = _onlyWithSalary.asStateFlow()

    private val _isError = MutableStateFlow(false)
    val isError: StateFlow<Boolean> = _isError.asStateFlow()

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
                    _industries.value = industries
                    firstList = industries
                    _isError.value = false
                } else {
                    _isError.value = true
                }
            } catch (e: IOException) {
                Log.e("FilterViewModel", "Ошибка при загрузке отраслей", e)
                _isError.value = true
            }
        }
    }

    fun onSelectIndustry(industry: Industry) {
        _selectedIndustry.value = industry
    }

    fun setSalary(salaryFilter: String) {
        _salary.value = salaryFilter
    }

    fun setOnlyWithSalary(onlyWithSalary: Boolean) {
        _onlyWithSalary.value = onlyWithSalary
    }

    fun resetFilters() {
        _selectedIndustry.value = null
        _salary.value = ""
        _onlyWithSalary.value = false
    }

    fun filterList(text: CharSequence?) {
        filterJob?.cancel()
        filterJob = viewModelScope.launch(Dispatchers.Default) {
            delay(FILTER_DELAY)
            if (text.isNullOrEmpty()) {
                resetList()
            } else {
                _industries.value = firstList.filter { it.name.contains(text, ignoreCase = true) }
            }
        }
    }

    fun resetList() {
        Log.d("list", "количество элементов сейчас: ${firstList.size}")
        _industries.value = firstList
    }

    fun setIndustry(area: String) {
        _workArea.value = area
        Log.d("area", "текст установлен на: ${workArea.value}")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("area", "ViewModel удалена")
    }

    companion object {
        const val FILTER_DELAY = 500L
    }
}
