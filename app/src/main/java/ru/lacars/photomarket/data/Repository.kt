package ru.lacars.photomarket.data

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import ru.lacars.photomarket.data.entity.Item
import ru.lacars.photomarket.data.network.APIService
import ru.lacars.photomarket.data.network.model.DashboardResponseDto
import ru.lacars.photomarket.data.network.model.GalleryResponseDto
import ru.lacars.photomarket.data.network.model.UploadPictureResponse
import java.io.InputStream
import javax.inject.Inject

class Repository @Inject constructor(
    private val apiService: APIService
) {

    suspend fun updateBarcodeById(id: String, barcode: String) {
        try {
            apiService.updateItemBarcodeById(id, barcode)
        } catch (e: Exception) {}
    }

    suspend fun useClearPicture(item: Item) {
        try {
            apiService.clearImage(item.id!!)
        } catch (e: Exception) {}
    }

    suspend fun deleteItem(item: Item) {
        try {
            apiService.deleteById(item.id!!)
        } catch (e: Exception) {}
    }

    fun getGalleryList(): Flow<DataState<List<GalleryResponseDto>>> = flow {
        emit(DataState.loading())
        val apiResponse = apiService.getGalleryList()

        if (apiResponse.isSuccessful && apiResponse.body() != null) {
            val galleries = mutableListOf<GalleryResponseDto>()
            apiResponse.body()?.forEach { item -> galleries.add(item) }

            if (galleries.isEmpty()) {
                emit(DataState.error("Upload fail"))
            } else {
                emit(DataState.success(galleries.toList()))
            }
        }
    }.flowOn(Dispatchers.IO)

    suspend fun uploadPictureTest(stream: InputStream?) {
        Log.d("TEST", "Repo upload")
        val part = MultipartBody.Part.createFormData(
            "imageFile", "myPic.jpg",
            RequestBody.create(
                "image/*".toMediaTypeOrNull(),
                stream!!.readBytes()
            )
        )
        apiService.uploadPictureTest(part)
    }

    suspend fun uploadPicture(stream: InputStream?): Flow<DataState<UploadPictureResponse>> = flow {
        Log.d("TEST", "Repo upload")
        val part = MultipartBody.Part.createFormData(
            "pic", "myPic",
            RequestBody.create(
                "image/*".toMediaTypeOrNull(),
                stream!!.readBytes()
            )
        )

        val apiResponse = apiService.uploadPicture(part)

        if (apiResponse.isSuccessful && apiResponse.body() != null) {
            val uploadPictureResponse = apiResponse.body()
            if (uploadPictureResponse == null) {
                emit(DataState.error("Upload fail"))
            } else {
                emit(DataState.success(uploadPictureResponse))
            }
        }
    }.flowOn(Dispatchers.IO)

    fun getDashboardFromNetwork(): Flow<DataState<DashboardResponseDto>> = flow {
        emit(DataState.loading())

        val apiResponse = apiService.getDashboard()

        if (apiResponse.isSuccessful && apiResponse.body() != null) {
            val dashboardDto = apiResponse.body()

            if (dashboardDto == null) {
                emit(DataState.error("No data from network"))
            } else {
                emit(DataState.success(dashboardDto))
            }
        }
    }.flowOn(Dispatchers.IO)
}
