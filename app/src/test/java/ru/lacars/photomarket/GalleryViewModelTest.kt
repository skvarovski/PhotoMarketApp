package ru.lacars.photomarket

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import ru.lacars.photomarket.data.DataState
import ru.lacars.photomarket.data.Repository
import ru.lacars.photomarket.data.entity.Item
import ru.lacars.photomarket.data.network.model.GalleryResponseDto
import ru.lacars.photomarket.ui.gallery.ContentState
import ru.lacars.photomarket.ui.gallery.GalleryViewModel
import ru.lacars.photomarket.ui.gallery.LoadingState

@ExperimentalCoroutinesApi
class GalleryViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    val coroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var getGalleryViewModel: GalleryViewModel
    private val mockGalleryDto = GalleryResponseDto("1", "1", "1", "1", "1")
    private val mockGalleryDto2 = GalleryResponseDto("2", "2", "2", "2", "2")
    private lateinit var viewModel: GalleryViewModel
    private lateinit var repository: Repository
    private val mockGalleries = mutableListOf<GalleryResponseDto>()
    private val dataList: MutableList<Item> = mutableListOf<Item>()

    @Before
    fun setup() {
        getGalleryViewModel = Mockito.mock(GalleryViewModel::class.java)
        mockGalleries.add(mockGalleryDto)
        mockGalleries.add(mockGalleryDto2)

        val mockRepositoryFlow: Flow<DataState<List<GalleryResponseDto>>> = flow {
            emit(DataState.loading())
            delay(10)
            emit(DataState.success(mockGalleries.toList()))
        }
        repository = Mockito.mock(Repository::class.java)
        Mockito.`when`(repository.getGalleryList())
            .thenReturn(mockRepositoryFlow)

        viewModel = GalleryViewModel(repository)
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
        // Convert DTO to Entity and Check Content DATA
        dataList.clear()
        mockGalleries.forEach { item ->
            dataList.add(Item(id = item.id?.toInt(), url = item.url, barcode = item.barcode, title = item.title, clear = item.clear))
        }
        Assert.assertEquals(dataList, viewModel.galleries.value)
    }
}
