package ru.lacars.catgallery.di.modules

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ru.lacars.photomarket.ui.MainActivity

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector
    abstract fun bindMainActivity(): MainActivity
}
