package ru.practicum.android.diploma.di

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import ru.practicum.android.diploma.data.repository.FilterRepositoryImpl
import ru.practicum.android.diploma.data.repository.VacancyRepositoryImpl
import ru.practicum.android.diploma.domain.FilterInteractor
import ru.practicum.android.diploma.domain.FilterRepository
import ru.practicum.android.diploma.domain.VacancyInteractor
import ru.practicum.android.diploma.domain.VacancyRepository
import ru.practicum.android.diploma.domain.interactor.FilterInteractorImpl
import ru.practicum.android.diploma.domain.interactor.VacancyInteractorImpl

val DomainModule = module {

    factoryOf(::VacancyRepositoryImpl) {
        bind<VacancyRepository>()
    }

    factoryOf(::VacancyInteractorImpl) {
        bind<VacancyInteractor>()
    }

    factoryOf(::FilterRepositoryImpl) {
        bind<FilterRepository>()
    }

    factoryOf(::FilterInteractorImpl) {
        bind<FilterInteractor>()
    }

}
