package ru.lacars.photomarket.ui.gallery

sealed class GalleryUiState

object LoadingState : GalleryUiState()
object ContentState : GalleryUiState()
class ErrorState(val message: String) : GalleryUiState()
