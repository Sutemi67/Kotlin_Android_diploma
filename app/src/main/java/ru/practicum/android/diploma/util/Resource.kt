package ru.practicum.android.diploma.util

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null,
    val itemsCount: Int? = null
) {
    class Success<T>(data: T, itemsCount: Int?) : Resource<T>(data, itemsCount = itemsCount)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
}
