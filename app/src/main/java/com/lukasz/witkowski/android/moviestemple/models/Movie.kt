package com.lukasz.witkowski.android.moviestemple.models

import android.net.Uri
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.lukasz.witkowski.android.moviestemple.utilities.POSTER_BASE_URI
import com.squareup.moshi.Json


data class Movie (
        var posterPath: String? = "",
        var id: Int = 0,
        var originalTitle: String = "",
        var title: String,
        var voteAverage: Float = 0f,
        var overview: String = "",
        var releaseDate: String = "",
        var genres: List<Genre> = emptyList(),
        var videos: List<Video> = emptyList(),
        var reviews: List<Review> = emptyList()
){
        val posterUri: Uri?
        get() {
                if(posterPath != null) {
                       return  Uri.parse(POSTER_BASE_URI + posterPath)
                }
                return null
        }
}