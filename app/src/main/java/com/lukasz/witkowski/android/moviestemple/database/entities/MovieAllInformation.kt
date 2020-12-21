package com.lukasz.witkowski.android.moviestemple.database.entities

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation


data class MovieAllInformation(
        @Embedded
        val movie: MovieEntity,

        @Relation(
                parentColumn = "movieId",
                entity = ActorEntity::class,
                entityColumn = "actorId",
                associateBy = Junction(
                        value = MovieWithActor::class,
                        parentColumn = "movieId",
                        entityColumn = "actorId"
                )
        )
        var cast: List<ActorEntity>,

        @Relation(
                parentColumn = "movieId",
                entity = GenreEntity::class,
                entityColumn = "genreId",
                associateBy = Junction(
                        value = MovieWithGenre::class,
                        parentColumn = "movieId",
                        entityColumn = "genreId"
                )
        )
        var genres: List<GenreEntity>,

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
                entity = DirectorEntity::class,
                entityColumn = "directorId",
                associateBy = Junction(
                        value = MovieWithDirector::class,
                        parentColumn = "movieId",
                        entityColumn = "directorId"
                )
        )
        var directors: List<DirectorEntity>,

        @Relation(
                parentColumn = "movieId",
                entity = WriterEntity::class,
                entityColumn = "writerId",
                associateBy = Junction(
                        value = MovieWithWriter::class,
                        parentColumn = "movieId",
                        entityColumn = "writerId"
                )
        )
        var writers: List<WriterEntity>
)