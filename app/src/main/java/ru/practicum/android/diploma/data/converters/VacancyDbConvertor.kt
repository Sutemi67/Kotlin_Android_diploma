package ru.practicum.android.diploma.data.converters

import ru.practicum.android.diploma.data.db.entity.VacancyEntity
import ru.practicum.android.diploma.domain.network.models.VacancyDetails

class VacancyDbConvertor {

    fun map(vacancy: VacancyDetails): VacancyEntity {
        return VacancyEntity(
            id = null,
            vacancy.name,
            vacancy.area.toString(),
            vacancy.salary.toString(),
            vacancy.employer.toString(),
            vacancy.snippet.toString(),
            vacancy.alternateUrl

        )
    }

}
