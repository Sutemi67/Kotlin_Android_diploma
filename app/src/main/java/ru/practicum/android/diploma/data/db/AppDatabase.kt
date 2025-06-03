package ru.practicum.android.diploma.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.practicum.android.diploma.data.db.dao.VacancyDoa
import ru.practicum.android.diploma.data.db.entity.VacancyEntity

@Database(version = 2, entities = [VacancyEntity::class], exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun vacancyDao(): VacancyDoa

}
