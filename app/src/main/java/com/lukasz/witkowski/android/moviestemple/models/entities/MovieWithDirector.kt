package com.lukasz.witkowski.android.moviestemple.models.entities

import androidx.room.Entity

@Entity(primaryKeys = ["movieId", "directorId"])
data class MovieWithDirector(
        var movieId: Int,
        var directorId: Int
)