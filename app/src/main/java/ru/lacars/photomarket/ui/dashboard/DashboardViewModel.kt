package ru.lacars.photomarket.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.lacars.photomarket.data.DataState
import ru.lacars.photomarket.data.Repository
import ru.lacars.photomarket.data.network.model.DashboardResponseDto
import javax.inject.Inject

class DashboardViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val _uiState = MutableStateFlow<DashboardUiState>(LoadingState)
    var uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    private val _dashboardData2 = MutableStateFlow<DashboardResponseDto?>(null)
    val dashboardData2: StateFlow<DashboardResponseDto?> = _dashboardData2.asStateFlow()

    fun init() {
        _uiState.value = LoadingState
        loadDashboard()
    }

    private fun loadDashboard() {
        viewModelScope.launch {
            repository.getDashboardFromNetwork().collect { dataState ->
                when (dataState) {
                    is DataState.Success -> {
                        _uiState.value = ContentState
                        _dashboardData2.value = dataState.data
                    }
                    is DataState.Error -> {
                        _uiState.value = ErrorState("error data received")
                        _dashboardData2.value = null
                    }
                    else -> {
                        _uiState.value = LoadingState
                        _dashboardData2.value = null
                    }
                }
            }
        }
    }
}
