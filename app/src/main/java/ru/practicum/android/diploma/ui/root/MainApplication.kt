package ru.practicum.android.diploma.ui.root

import android.app.Application
import org.koin.core.context.startKoin
import ru.practicum.android.diploma.di.AppModule
import ru.practicum.android.diploma.di.DataModule
import ru.practicum.android.diploma.di.DomainModule

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            this@MainApplication
            modules(AppModule, DomainModule, DataModule)
        }
    }
}
