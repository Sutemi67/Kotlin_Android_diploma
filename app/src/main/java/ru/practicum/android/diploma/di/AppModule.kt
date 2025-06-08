package ru.practicum.android.diploma.di

import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.data.converters.VacancyDbConvertor
import ru.practicum.android.diploma.ui.screens.favourites.FavouritesViewModel
import ru.practicum.android.diploma.ui.screens.filter.FilterViewModel
import ru.practicum.android.diploma.ui.screens.main.MainViewModel
import ru.practicum.android.diploma.ui.screens.vacancy.VacancyDetailsViewModel

val AppModule = module {

    viewModel<MainViewModel> { MainViewModel(interactor = get()) }
    viewModel<FilterViewModel> { FilterViewModel() }
    viewModel<VacancyDetailsViewModel> {
        VacancyDetailsViewModel(
            interactor = get(), connectManager = get(), converter = get()
        )
    }

    viewModel<FavouritesViewModel> { FavouritesViewModel(interactor = get()) }

    singleOf(::VacancyDbConvertor)
}
