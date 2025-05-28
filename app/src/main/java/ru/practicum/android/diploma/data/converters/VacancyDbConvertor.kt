package ru.practicum.android.diploma.data.converters

import ru.practicum.android.diploma.data.db.entity.VacancyEntity

class VacancyDbConvertor {

    fun map(vacancy: VacancyEntity) {
        vacancy.id
        vacancy.name
    }

}
