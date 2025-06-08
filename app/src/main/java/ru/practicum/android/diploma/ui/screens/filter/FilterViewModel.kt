package ru.practicum.android.diploma.ui.screens.filter

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FilterViewModel : ViewModel() {
    private var _workArea = MutableStateFlow("Отрасль")
    val workArea: StateFlow<String> = _workArea.asStateFlow()

    fun setWorkingArea(area: String) {
        _workArea.value = area
        Log.d("area", "текст установлен на: ${workArea.value}")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("area", "ViewModel удалена")
    }
}
