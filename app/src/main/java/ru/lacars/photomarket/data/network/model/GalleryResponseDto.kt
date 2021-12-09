package ru.lacars.photomarket.data.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GalleryResponseDto(
    @Json(name = "id") val id: String?,
    @Json(name = "url") val url: String?,
    @Json(name = "title") val title: String?,
    @Json(name = "barcode") val barcode: String?,
    @Json(name = "clear") val clear: String?
)
