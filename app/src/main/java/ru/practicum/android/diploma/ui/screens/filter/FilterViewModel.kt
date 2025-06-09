package ru.practicum.android.diploma.ui.screens.filter

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.domain.network.models.Industry

class FilterViewModel(
    private val networkClient: NetworkClient
) : ViewModel() {
    private var _workArea = MutableStateFlow("Отрасль")
    val workArea: StateFlow<String> = _workArea.asStateFlow()

    private var _industries = MutableStateFlow<List<Industry>>(emptyList())
    val industries: StateFlow<List<Industry>> = _industries.asStateFlow()

    init {
        loadIndustries()
    }

    private fun loadIndustries() {
        viewModelScope.launch {
            networkClient.getIndustries()?.let { industries ->
                _industries.value = industries
            }
        }
    }

    fun onSelectIndustry(industry: Industry) {
        val list = industries.value
        _industries.value = list.filter { it.id == industry.id }
    }

    fun setWorkingArea(area: String) {
        _workArea.value = area
        Log.d("area", "текст установлен на: ${workArea.value}")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("area", "ViewModel удалена")
    }
}
