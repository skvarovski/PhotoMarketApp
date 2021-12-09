package ru.lacars.photomarket.base

import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.android.AndroidInjection
import ru.lacars.photomarket.BuildConfig
import javax.inject.Inject

abstract class BaseActivity<VM : ViewModel> : AppCompatActivity() {

    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory

    protected val activityViewModel by lazy { getViewModel() }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    abstract fun getViewModel(): VM

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN && BuildConfig.DEBUG) {
            // Some code for Debugg
        }
        return super.onKeyDown(keyCode, event)
    }
}
