package com.lukasz.witkowski.android.moviestemple.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Movies")
data class MovieEntity(
        var posterPath: String? = "",
        @PrimaryKey
        var movieId: Int = 0,
        var originalTitle: String = "",
        var title: String,
        var voteAverage: Float = 0f,
        var voteCount: Int = 0,
        var overview: String = "",
        var releaseDate: String = "",
)