package com.example.android.popularmovies.models

import com.squareup.moshi.Json

data class RecommendationsResponse(
        var page: Int,
        var results: List<Movie>,
        @Json(name = "total_pages")
        var totalPages: Int,
        @Json(name = "total_results")
        var totalResults: Int
)