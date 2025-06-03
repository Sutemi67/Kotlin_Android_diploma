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

    @Query("SELECT * FROM vacancy_table WHERE id = :vacancyId")
    suspend fun getVacancyById(vacancyId: Int): VacancyEntity?

    @Query("DELETE FROM vacancy_table WHERE id = :vacancyId")
    suspend fun deleteVacancy(vacancyId: Int)
}
