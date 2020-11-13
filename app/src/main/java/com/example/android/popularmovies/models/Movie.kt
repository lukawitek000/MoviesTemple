package com.example.android.popularmovies.models

import android.net.Uri

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
)