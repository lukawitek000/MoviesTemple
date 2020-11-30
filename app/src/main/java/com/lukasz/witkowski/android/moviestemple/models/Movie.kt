package com.lukasz.witkowski.android.moviestemple.models

import android.net.Uri
import com.lukasz.witkowski.android.moviestemple.api.POSTER_BASE_URI


data class Movie (
        var posterPath: String? = "",
        var id: Int = 0,
        var originalTitle: String = "",
        var title: String,
        var voteAverage: Float = 0f,
        var voteCount: Int = 0,
        var overview: String = "",
        var releaseDate: String = "",
        var genres: List<Genre> = emptyList(),
        var videos: List<Video> = emptyList(),
        var reviews: List<Review> = emptyList(),
        var cast: List<Actor> = emptyList(),
        var directors: List<Director> = emptyList(),
        var writers: List<Writer> = emptyList()
){
        val posterUri: Uri?
        get() {
                if(posterPath != null) {
                       return  Uri.parse(POSTER_BASE_URI + posterPath)
                }
                return null
        }
}