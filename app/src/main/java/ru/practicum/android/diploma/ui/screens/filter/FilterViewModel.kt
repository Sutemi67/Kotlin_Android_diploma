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
import ru.practicum.android.diploma.domain.network.models.Industry

class FilterViewModel(
    private val networkClient: NetworkClient
) : ViewModel() {
    private var _workArea = MutableStateFlow("Отрасль")
    val workArea: StateFlow<String> = _workArea.asStateFlow()

    private var _industries = MutableStateFlow<List<Industry>>(emptyList())
    val industries: StateFlow<List<Industry>> = _industries.asStateFlow()

    private var firstList: List<Industry> = emptyList()

    private var filterJob: Job? = null

    init {
        loadIndustries()
    }

    private fun loadIndustries() {
        viewModelScope.launch {
            networkClient.getIndustries()?.let { industries ->
                _industries.value = industries
                firstList = industries
            }
        }
    }

    fun onSelectIndustry(industry: Industry) {
        val list = industries.value
        _industries.value = list.filter { it.id == industry.id }
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
