package com.example.android.popularmovies.models

import com.squareup.moshi.Json

data class TMDBResponse(
        var page: Int,
        @Json(name="total_results")
        var totalResults: Int,
        @Json(name="total_pages")
        var totalPages: Int,
        @Json(name="results")
        var movies: List<Movie>)