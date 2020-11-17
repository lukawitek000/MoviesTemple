package com.example.android.popularmovies.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation


data class MovieWithReviewsAndVideos(
        @Embedded
        val movie: Movie,
        @Relation(
                parentColumn = "movieID",
                entityColumn = "movieOwnerID",
                entity = Review::class
        )
        val reviews: List<Review>,
        @Relation(
                parentColumn = "movieID",
                entityColumn = "movieOwnerID",
                entity = Video::class
        )
        val videos: List<Video>
)