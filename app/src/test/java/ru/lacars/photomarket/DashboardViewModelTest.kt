package ru.lacars.photomarket

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import ru.lacars.photomarket.data.DataState
import ru.lacars.photomarket.data.Repository
import ru.lacars.photomarket.data.network.model.DashboardResponseDto
import ru.lacars.photomarket.ui.dashboard.ContentState
import ru.lacars.photomarket.ui.dashboard.DashboardViewModel
import ru.lacars.photomarket.ui.dashboard.LoadingState

@ExperimentalCoroutinesApi
class DashboardViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    val coroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var getDashboardViewModel: DashboardViewModel
    private val mockDashboardDto = DashboardResponseDto("1", "1", "1", "1")
    private lateinit var viewModel: DashboardViewModel
    private lateinit var repository: Repository

    @Before
    fun setup() {
        getDashboardViewModel = Mockito.mock(DashboardViewModel::class.java)

        val mockRepositoryFlow: Flow<DataState<DashboardResponseDto>> = flow {
            emit(DataState.loading())
            delay(10)
            emit(DataState.success(mockDashboardDto))
        }
        repository = Mockito.mock(Repository::class.java)
        Mockito.`when`(repository.getDashboardFromNetwork())
            .thenReturn(mockRepositoryFlow)

        viewModel = DashboardViewModel(repository)
    }

    @Test
    fun viewModelInitTest() = coroutineRule.testDispatcher.runBlockingTest {
        // init ViewModel
        viewModel.init()
        // Loading Content
        assert(viewModel.uiState.value is LoadingState)
        // wait 10 mills
        advanceTimeBy(10)
        // Content Load
        assert(viewModel.uiState.value is ContentState)
    }

    @Test
    fun viewModelDataTest() = coroutineRule.testDispatcher.runBlockingTest {
        // init ViewModel
        viewModel.init()
        // Loading Content
        assert(viewModel.uiState.value is LoadingState)
        // wait 10 mills
        advanceTimeBy(10)
        // Check Content DATA
        assertEquals(mockDashboardDto, viewModel.dashboardData2.value)
    }
}
