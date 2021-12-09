package ru.lacars.photomarket.data.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DashboardResponseDto(
    @Json(name = "count_total") val countTotal: String?,
    @Json(name = "count_barcode") val countBarcode: String?,
    @Json(name = "count_clear") val countClear: String?,
    @Json(name = "last_update") val lastUpdate: String?,
    // @Json(name = "clear") val clear: Int?
)
