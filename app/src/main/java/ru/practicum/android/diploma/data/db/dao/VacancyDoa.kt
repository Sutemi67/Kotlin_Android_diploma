package ru.practicum.android.diploma.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.practicum.android.diploma.data.db.entity.VacancyEntity

@Dao
interface VacancyDoa {

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun addVacancy(vacancy: VacancyEntity)

    @Query("SELECT * FROM vacancy_table")
    suspend fun getAllVacancy(): List<VacancyEntity>

}
