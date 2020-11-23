package com.lukasz.witkowski.android.moviestemple.models

import com.squareup.moshi.Json

data class ReviewResponse(
        @Transient
        var id: Long = 0L,
        var page: Int,
        var results: List<Review>,
        @Json(name = "total_pages")
        var totalPages: Int,
        @Json(name = "total_results")
        var totalResults: Int
)