package ru.practicum.android.diploma.data.converters

import com.google.gson.Gson
import ru.practicum.android.diploma.data.db.entity.VacancyEntity
import ru.practicum.android.diploma.domain.network.models.VacancyDetails

class VacancyDbConvertor(private val gson: Gson) {
    fun mapToEntity(vacancy: VacancyDetails): VacancyEntity {
        return VacancyEntity(
            id = vacancy.id.toInt(),
            vacancyJson = gson.toJson(vacancy)
        )
    }

    fun mapToDomain(entity: VacancyEntity): VacancyDetails {
        return gson.fromJson(entity.vacancyJson, VacancyDetails::class.java)
    }
}
