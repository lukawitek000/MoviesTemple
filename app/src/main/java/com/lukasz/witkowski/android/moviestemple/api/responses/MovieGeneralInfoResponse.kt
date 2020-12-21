package com.lukasz.witkowski.android.moviestemple.api.responses

import com.squareup.moshi.Json

data class MovieGeneralInfoResponse(
        @Json(name="poster_path")
        var posterPath: String? = "",
        var adult: Boolean = false,
        var overview: String = "",
        @Json(name="release_date")
        var releaseDate: String = "",
        @Json(name="genre_ids")
        var genreIds: List<Long> = emptyList(),
        var id: Int = 0,
        @Json(name="original_title")
        var originalTitle: String = "",
        @Json(name="original_language")
        var originalLanguage: String = "",
        var title: String,
        @Json(name="backdrop_path")
        var backdropPath: String? = "",
        var popularity: Float = 0.0f,
        @Json(name="vote_count")
        var voteCount: Int = 0,
        var video: Boolean = false,
        @Json(name="vote_average")
        var voteAverage: Float = 0f,
)