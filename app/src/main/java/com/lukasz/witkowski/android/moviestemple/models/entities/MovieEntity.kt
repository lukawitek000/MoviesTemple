package com.lukasz.witkowski.android.moviestemple.models

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.squareup.moshi.Json


@Entity
data class MovieEntity(
        var posterPath: String? = "",
        @PrimaryKey
        var id: Int = 0,
        var originalTitle: String = "",
        var title: String,
        var voteAverage: Float = 0f,
        var overview: String = "",
        var releaseDate: String = "",
)