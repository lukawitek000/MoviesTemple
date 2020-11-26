package com.lukasz.witkowski.android.moviestemple.models.entities

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.lukasz.witkowski.android.moviestemple.models.Genre
import com.lukasz.witkowski.android.moviestemple.models.Movie
import com.lukasz.witkowski.android.moviestemple.models.Review
import com.lukasz.witkowski.android.moviestemple.models.Video


data class MovieWithGenresReviewsAndVideos(
        @Embedded
        val movie: MovieEntity,

        @Relation(
                parentColumn = "movieId",
                entity = Genre::class,
                entityColumn = "genreId",
                associateBy = Junction(
                        value = MovieWithGenre::class,
                        parentColumn = "movieId",
                        entityColumn = "genreId"
                )
        )
        var genres: List<Genre>,

        @Relation(
                parentColumn = "movieId",
                entityColumn = "movieOwnerID",
                entity = ReviewEntity::class
        )
        val reviews: List<ReviewEntity>,
        @Relation(
                parentColumn = "movieId",
                entityColumn = "movieOwnerID",
                entity = VideoEntity::class
        )
        val videos: List<VideoEntity>
)