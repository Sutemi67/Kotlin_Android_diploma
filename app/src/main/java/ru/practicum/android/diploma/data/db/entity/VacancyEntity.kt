package ru.practicum.android.diploma.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import ru.practicum.android.diploma.domain.network.models.Area
import ru.practicum.android.diploma.domain.network.models.Employer
import ru.practicum.android.diploma.domain.network.models.Salary
import ru.practicum.android.diploma.domain.network.models.Snippet

@Entity(tableName = "vacancy_table")
data class VacancyEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val name: String,
    val area: Area,
    val salary: Salary?,
    val employer: Employer,
    val snippet: Snippet,
    val alternateUrl: String,
)
