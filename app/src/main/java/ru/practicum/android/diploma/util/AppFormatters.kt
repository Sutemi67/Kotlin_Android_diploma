package ru.practicum.android.diploma.util

object AppFormatters {
    const val ONE = 1
    const val TWO = 2
    const val FOUR = 4
    const val TEN = 10

    fun vacanciesCountTextFormatter(count: String): String {
        val count = count.toInt()
        return when (count % TEN) {
            ONE -> "Найдена $count вакансия"
            in TWO..FOUR -> "Найдено $count вакансии"
            else -> "Найдено $count вакансий"
        }
    }
}
