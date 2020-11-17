package com.example.android.popularmovies.models

import com.squareup.moshi.Json

data class ReviewResponse(
        var id: Long,
        var page: Int,
        var results: List<Review>,
        @Json(name = "total_pages")
        var totalPages: Int,
        @Json(name = "total_results")
        var totalResults: Int
)