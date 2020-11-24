package com.lukasz.witkowski.android.moviestemple.models.responses

import com.squareup.moshi.Json

data class ReviewsListResponse(
        var page: Int,
        var results: List<ReviewResponse>,
        @Json(name = "total_pages")
        var totalPages: Int,
        @Json(name = "total_results")
        var totalResults: Int
)