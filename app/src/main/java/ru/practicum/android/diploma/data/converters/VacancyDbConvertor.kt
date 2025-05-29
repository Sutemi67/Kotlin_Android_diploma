package ru.practicum.android.diploma.data.converters

import ru.practicum.android.diploma.data.db.entity.VacancyEntity
import ru.practicum.android.diploma.domain.network.models.VacancyDetails

class VacancyDbConvertor {

    fun map(vacancy: VacancyDetails) : VacancyEntity {
        return VacancyEntity(
            id = null,
            vacancy.name,
            vacancy.area,
            vacancy.salary,
            vacancy.employer,
            vacancy.snippet,
            vacancy.alternateUrl

        )
    }

}
