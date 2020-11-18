package com.example.android.popularmovies.models

import android.net.Uri
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.android.popularmovies.utilities.POSTER_BASE_URI
import com.squareup.moshi.Json


@Entity
data class Movie (
        var popularity: Float = 0.0f,
        @Json(name="vote_count")
        var voteCount: Int = 0,
        var video: Boolean = false,
        @Json(name="poster_path")
        var posterPath: String = "",
        @PrimaryKey
        var id: Long = 0L,
        var adult: Boolean = false,
        @Json(name="backdrop_path")
        var backdropPath: String = "",
        @Json(name="original_language")
        var originalLanguage: String = "",
        @Json(name="original_title")
        var originalTitle: String = "",
        @Json(name="genre_ids")
        @Ignore
        var genreIds: List<Long> = emptyList(),
        var title: String,
        @Json(name="vote_average")
        var voteAverage: Float = 0f,
        var overview: String = "",
        @Json(name="release_date")
        var releaseDate: String = "",
        @Transient
        @Ignore
        var trailers: List<Video> = emptyList(),
        @Transient
        @Ignore
        var reviews: List<Review> = emptyList()
){
        val posterUri: Uri
        get() = Uri.parse(POSTER_BASE_URI + posterPath)

        constructor(): this( 0f, 0, false, "", 0L,
               false, "", "", "", emptyList(), "",
               0f, "", "", emptyList(), emptyList())
}