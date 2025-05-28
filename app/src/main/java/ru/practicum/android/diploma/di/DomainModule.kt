package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.data.repository.VacancyRepository
import ru.practicum.android.diploma.domain.VacancyRepositoryInterface

val DomainModule = module {
    single<VacancyRepositoryInterface> { VacancyRepository(get()) }
}
