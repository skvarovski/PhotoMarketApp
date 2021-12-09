package ru.lacars.photomarket.ui.barcode

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.lacars.photomarket.data.Repository
import javax.inject.Inject

class BarcodeViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    fun updateBarcode(id: String, barcode: String) {
        viewModelScope.launch {
            repository.updateBarcodeById(id, barcode)
        }
    }
}
