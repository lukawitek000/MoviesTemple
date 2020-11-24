package com.lukasz.witkowski.android.moviestemple.models.responses

import com.squareup.moshi.Json

data class TMDBResponse(
        var page: Int = 1,
        @Json(name="total_results")
        var totalResults: Int = 0,
        @Json(name="total_pages")
        var totalPages: Int = 1,
        @Json(name="results")
        var movies: List<MovieGeneralInfoResponse> = emptyList())