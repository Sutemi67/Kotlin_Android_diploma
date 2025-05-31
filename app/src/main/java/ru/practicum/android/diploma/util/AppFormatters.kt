package ru.practicum.android.diploma.util

object AppFormatters {
    fun vacanciesCountTextFormatter(count: String): String {
        val count = count.toInt()
        return when (count % 10) {
            1 -> "Найдена $count вакансия"
            in 2..4 -> "Найдено $count вакансии"
            else -> "Найдено $count вакансий"
        }
    }
}
