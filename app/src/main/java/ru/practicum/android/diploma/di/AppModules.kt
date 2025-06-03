package ru.practicum.android.diploma.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.ui.screens.main.MainViewModel
import ru.practicum.android.diploma.ui.screens.vacancy.VacancyDetailsViewModel

val AppModule = module {

    viewModel<MainViewModel> {
        MainViewModel(
            interactor = get()
        )
    }

    viewModel<VacancyDetailsViewModel> { VacancyDetailsViewModel(get()) }

}
