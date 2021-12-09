package ru.lacars.photomarket.di.modules

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ru.lacars.photomarket.ui.barcode.BarcodeFragment
import ru.lacars.photomarket.ui.dashboard.DashboardFragment
import ru.lacars.photomarket.ui.gallery.GalleryFragment

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract fun bindDashboardFragment(): DashboardFragment

    @ContributesAndroidInjector
    abstract fun bindGalleryFragment(): GalleryFragment

    @ContributesAndroidInjector
    abstract fun bindBarcodeFragment(): BarcodeFragment
}
