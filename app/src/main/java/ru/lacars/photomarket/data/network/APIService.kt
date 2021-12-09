package ru.lacars.photomarket.data.network

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query
import ru.lacars.photomarket.data.network.model.DashboardResponseDto
import ru.lacars.photomarket.data.network.model.GalleryResponseDto
import ru.lacars.photomarket.data.network.model.UploadPictureResponse

interface APIService {

    @POST("/api/item-barcode-by-id")
    @FormUrlEncoded
    suspend fun updateItemBarcodeById(@Field("id") id: String, @Field("barcode") barcode: String)

    @GET("/api/get-gallery")
    suspend fun getGalleryList(): Response<List<GalleryResponseDto>>

    @GET("/api/dashboard")
    suspend fun getDashboard(): Response<DashboardResponseDto>

    @POST("/api/gallery-delete-by-id")
    suspend fun deleteById(@Query("id") id: Int)

    @GET("/api/gallery-clear-by-id")
    suspend fun clearImage(@Query("id") id: Int)

    @Multipart
    @POST("/api/upload-picture")
    suspend fun uploadPicture(
        @Part part: MultipartBody.Part
    ): Response<UploadPictureResponse>

    @Multipart
    @POST("/api/upload-picture")
    suspend fun uploadPictureTest(@Part part: MultipartBody.Part): Response<UploadPictureResponse>
}
