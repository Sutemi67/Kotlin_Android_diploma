package ru.practicum.android.diploma.di

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import ru.practicum.android.diploma.data.repository.VacancyRepository
import ru.practicum.android.diploma.domain.VacancyInteractorInterface
import ru.practicum.android.diploma.domain.VacancyRepositoryInterface
import ru.practicum.android.diploma.domain.interactor.VacancyInteractorImpl

val DomainModule = module {

    factoryOf(::VacancyRepository) {
        bind<VacancyRepositoryInterface>()
    }

    factoryOf(::VacancyInteractorImpl) {
        bind<VacancyInteractorInterface>()
    }

}
