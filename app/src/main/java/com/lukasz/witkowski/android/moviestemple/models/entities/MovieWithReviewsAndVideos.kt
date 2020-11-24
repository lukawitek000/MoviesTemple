package com.lukasz.witkowski.android.moviestemple.models.entities

import androidx.room.Embedded
import androidx.room.Relation
import com.lukasz.witkowski.android.moviestemple.models.Movie
import com.lukasz.witkowski.android.moviestemple.models.Review
import com.lukasz.witkowski.android.moviestemple.models.Video


data class MovieWithReviewsAndVideos(
        @Embedded
        val movie: MovieEntity,
        @Relation(
                parentColumn = "id",
                entityColumn = "movieOwnerID",
                entity = ReviewEntity::class
        )
        val reviews: List<ReviewEntity>,
        @Relation(
                parentColumn = "id",
                entityColumn = "movieOwnerID",
                entity = VideoEntity::class
        )
        val videos: List<VideoEntity>
)