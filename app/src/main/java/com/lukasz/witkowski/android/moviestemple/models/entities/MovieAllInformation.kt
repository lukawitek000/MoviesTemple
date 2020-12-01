package com.lukasz.witkowski.android.moviestemple.models.entities

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.lukasz.witkowski.android.moviestemple.models.*


data class MovieAllInformation(
        @Embedded
        val movie: MovieEntity,

        @Relation(
                parentColumn = "movieId",
                entity = Actor::class,
                entityColumn = "actorId",
                associateBy = Junction(
                        value = MovieWithActor::class,
                        parentColumn = "movieId",
                        entityColumn = "actorId"
                )
        )
        var cast: List<Actor>,

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
        val videos: List<VideoEntity>,


        @Relation(
                parentColumn = "movieId",
                entity = Director::class,
                entityColumn = "directorId",
                associateBy = Junction(
                        value = MovieWithDirector::class,
                        parentColumn = "movieId",
                        entityColumn = "directorId"
                )
        )
        var directors: List<Director>,

        @Relation(
                parentColumn = "movieId",
                entity = Writer::class,
                entityColumn = "writerId",
                associateBy = Junction(
                        value = MovieWithWriter::class,
                        parentColumn = "movieId",
                        entityColumn = "writerId"
                )
        )
        var writers: List<Writer>
)