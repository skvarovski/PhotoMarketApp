package ru.lacars.photomarket.ui.gallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.lacars.photomarket.data.DataState
import ru.lacars.photomarket.data.Repository
import ru.lacars.photomarket.data.entity.Item
import javax.inject.Inject

class GalleryViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val _uiState = MutableStateFlow<GalleryUiState>(LoadingState)
    var uiState: StateFlow<GalleryUiState> = _uiState.asStateFlow()

    private val _galleries = MutableStateFlow<List<Item>?>(emptyList())
    val galleries: StateFlow<List<Item>?> = _galleries.asStateFlow()

    private val data: MutableList<Item> = mutableListOf<Item>()

    fun init() {

        loadGallery()
    }

    fun clearByImage(item: Item) {
        viewModelScope.launch {
            repository.useClearPicture(item)
            loadGallery()
        }
    }

    fun deleteByItem(item: Item) {
        viewModelScope.launch {
            repository.deleteItem(item)
            loadGallery()
        }
    }
    private fun loadGallery() {
        viewModelScope.launch {
            repository.getGalleryList().collect { dataState ->
                when (dataState) {
                    is DataState.Success -> {
                        data.clear()
                        dataState.data.forEach { item ->
                            data.add(Item(id = item.id?.toInt(), url = item.url, barcode = item.barcode, title = item.title, clear = item.clear))
                        }
                        _galleries.emit(data)
                        _uiState.emit(ContentState)
                    }
                    is DataState.Loading -> {
                        _uiState.emit(LoadingState)
                        _galleries.emit(null)
                    }
                    else -> {
                        _uiState.emit(ErrorState("error receive data"))
                        _galleries.emit(null)
                    }
                }
            }
        }
    }
}
