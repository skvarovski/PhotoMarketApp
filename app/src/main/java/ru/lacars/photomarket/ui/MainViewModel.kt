package ru.lacars.photomarket.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import ru.lacars.photomarket.data.Repository
import java.io.InputStream
import javax.inject.Inject

class MainViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    suspend fun uploadPicture(stream: InputStream?) {
        // repository.uploadPicture(stream)
        repository.uploadPictureTest(stream)
        Log.d("TEST", "mainViewModel launch")
    }
}
