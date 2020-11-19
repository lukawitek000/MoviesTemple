package com.example.android.popularmovies.models

import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

data class MovieDetailsResponse(
        var adult: Boolean = false,
        @Json(name="backdrop_path")
        var backdropPath: String? = "",
        @Json(name="belongs_to_collection")
        var belongsToCollection: Any? = null,
        var budget: Long = 0L,
        var genres: List<Genre> = emptyList(),
        var homePage: String? = null,
        @PrimaryKey
        var id: Long = 0L,
        @Json(name="imdb_id")
        var imdbID: String? = null,
        @Json(name="original_language")
        var originalLanguage: String = "",
        @Json(name="original_title")
        var originalTitle: String = "",
        var overview: String = "",
        var popularity: Float = 0.0f,
        @Json(name="poster_path")
        var posterPath: String? = null,
        @Json(name = "production_companies")
        var productionCompany: List<ProductionCompany> = emptyList(),
        var productionCountries: List<ProductionCountries> = emptyList(),
        @Json(name="release_date")
        var releaseDate: String = "",
        var revenue: Long = 0L,
        var runtime: Int? = null,
        var spokenLanguages: List<SpokenLanguage> = emptyList(),
        var status: String = "",
        var tagline: String? = null,
        var title: String,
        var video: Boolean = false,
        @Json(name="vote_average")
        var voteAverage: Float = 0f,
        @Json(name="vote_count")
        var voteCount: Int = 0,

        var videos: VideoResponse,
        var reviews: ReviewResponse,
        var recommendations: RecommendationsResponse

)