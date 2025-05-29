package ru.practicum.android.diploma.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vacancy_table")
data class VacancyEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val name: String,
    val area: String,
    val salary: String?,
    val employer: String,
    val snippet: String,
    val alternateUrl: String,
)
