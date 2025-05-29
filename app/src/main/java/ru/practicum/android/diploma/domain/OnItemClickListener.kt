package ru.practicum.android.diploma.domain

fun interface OnItemClickListener <T> {
    fun onItemClick(item : T)
}
