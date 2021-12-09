package ru.lacars.photomarket.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import ru.lacars.catgallery.di.modules.ActivityModule
import ru.lacars.photomarket.App
import ru.lacars.photomarket.di.modules.FragmentModule
import ru.lacars.photomarket.di.modules.NetworkModule
import ru.lacars.photomarket.di.modules.ViewModelModule
import ru.lacars.photomarket.di.modules.ViewModelsFactoryModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        NetworkModule::class,
        // DownloadModule::class,
        ActivityModule::class,
        FragmentModule::class,
        ViewModelsFactoryModule::class,
        ViewModelModule::class
    ]
)
interface AppComponent : AndroidInjector<App> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun create(app: Application): Builder

        fun build(): AppComponent
    }

    override fun inject(app: App)
}
