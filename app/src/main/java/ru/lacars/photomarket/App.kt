package ru.lacars.photomarket

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import ru.lacars.photomarket.di.DaggerAppComponent
import ru.lacars.photomarket.util.isNight
import javax.inject.Inject

class App : Application(), HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()
        initDi()
        setupDayNightMode()
    }

    fun initDi() {
        DaggerAppComponent.builder()
            .create(this)
            .build()
            .inject(this)
    }
    override fun androidInjector(): AndroidInjector<Any> {
        return androidInjector
    }

    fun setupDayNightMode() {
        val mode = if (isNight()) {
            AppCompatDelegate.MODE_NIGHT_YES
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }
        AppCompatDelegate.setDefaultNightMode(mode)
    }
}
