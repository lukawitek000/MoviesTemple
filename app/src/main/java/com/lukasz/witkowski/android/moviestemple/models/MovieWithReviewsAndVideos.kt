package com.lukasz.witkowski.android.moviestemple.models

import androidx.room.Embedded
import androidx.room.Relation


data class MovieWithReviewsAndVideos(
        @Embedded
        val movie: Movie,
        @Relation(
                parentColumn = "id",
                entityColumn = "movieOwnerID",
                entity = Review::class
        )
        val reviews: List<Review>,
        @Relation(
                parentColumn = "id",
                entityColumn = "movieOwnerID",
                entity = Video::class
        )
        val videos: List<Video>
)