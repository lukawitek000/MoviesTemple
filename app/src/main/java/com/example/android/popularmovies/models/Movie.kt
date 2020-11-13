package com.example.android.popularmovies

import android.net.Uri
import com.example.android.popularmovies.Review

data class Movie(
    var id: Int = 0,
    var originalTitle: String,
    var title: String,
    var poster: Uri,
    var overview: String,
    var voteAverage: Float = 0.0f,
    var releaseDate: String,
    var videoUrls: Array<String>,
    var reviews: Array<Review>,
    var isFavourite: Boolean = false
)