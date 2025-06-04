package ru.practicum.android.diploma.util

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null,
    val itemsCount: Int? = null
) {
    class Success<T>(data: T, itemsCount: Int?) : Resource<T>(data, null, itemsCount)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message, null)
}
