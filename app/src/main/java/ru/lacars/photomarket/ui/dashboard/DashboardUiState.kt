package ru.lacars.photomarket.ui.dashboard

sealed class DashboardUiState

object LoadingState : DashboardUiState()
object ContentState : DashboardUiState()
class ErrorState(val message: String) : DashboardUiState()
