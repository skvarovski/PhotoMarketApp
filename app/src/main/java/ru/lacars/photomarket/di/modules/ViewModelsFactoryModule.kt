package ru.lacars.photomarket.di.modules

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import ru.lacars.photomarket.di.factories.ViewModelProviderFactory

/**
 * This modules is responsible for creating ViewModels for screens with the help of SwvlViewModelProvider Factory.
 */
@Module
interface ViewModelsFactoryModule {

    @Binds
    fun bindViewModelFactory(factory: ViewModelProviderFactory): ViewModelProvider.Factory
}
