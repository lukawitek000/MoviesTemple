package com.example.android.popularmovies.models

import android.net.Uri
import com.squareup.moshi.Json

/*
data class Movie(
        var id: Int = 0,
        var originalTitle:String = "",
        var title: String = "",
        var poster: Uri? = null,
        var overview: String = "",
        var voteAverage: Float = 0.0f,
        var releaseDate: String = "",
        var videoUrls: Array<String> = emptyArray<String>(),
        var reviews: Array<Review> = emptyArray<Review>(),
        var isFavourite: Boolean = false
)*/

data class Movie (
        var popularity: Float,
        @Json(name="vote_count")
        var voteCount: Int,
        var video: Boolean,
        @Json(name="poster_path")
        var posterPath: String,
        var id: Long,
        var adult: Boolean,
        @Json(name="backdrop_path")
        var backdropPath: String,
        @Json(name="original_language")
        var originalLanguage: String,
        @Json(name="original_title")
        var originalTitle: String,
        @Json(name="genre_ids")
        var genreIds: List<Long>,
        var title: String,
        @Json(name="vote_average")
        var voteAverage: Float,
        var overview: String,
        @Json(name="release_date")
        var releaseDate: String,
        @Transient
        var trailers: List<Video> = emptyList(),
        @Transient
        var reviews: List<Review> = emptyList()
)