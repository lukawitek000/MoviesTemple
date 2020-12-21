package com.lukasz.witkowski.android.moviestemple.database.entities

import androidx.room.Entity

@Entity(primaryKeys = ["movieId", "directorId"])
data class MovieWithDirector(
        var movieId: Int,
        var directorId: Int
)