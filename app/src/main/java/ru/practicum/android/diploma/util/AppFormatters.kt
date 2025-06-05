package ru.practicum.android.diploma.util

import android.content.Context
import ru.practicum.android.diploma.R

object AppFormatters {

    fun vacanciesCountTextFormatter(context: Context, count: Int): String {
        return context.resources.getQuantityString(
            R.plurals.vacancies_count,
            count,
            count
        )
    }

    fun getCurrencyIcon(currencyCode: String): String {
        return when (currencyCode.uppercase()) {
            "USD" -> "$"
            "EUR" -> "€"
            "RUR" -> "₽"
            "GBP" -> "£"
            "JPY" -> "¥"
            "BYR" -> "Br"
            "KZT" -> "₸"
            "UZS" -> "сум"
            "KGS" -> "сом"
            else -> currencyCode
        }
    }

}
