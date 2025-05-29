package ru.practicum.android.diploma.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.ui.screens.main.MainViewModel

val AppModule = module {

    viewModel<MainViewModel> {
        MainViewModel(
            interactor = get()
        )
    }

}
