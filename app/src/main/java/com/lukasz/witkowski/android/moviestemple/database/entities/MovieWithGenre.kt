package com.lukasz.witkowski.android.moviestemple.database.entities

import androidx.room.Entity

@Entity(primaryKeys = ["movieId", "genreId"])
data class MovieWithGenre(
        val movieId: Int,
        val genreId: Int
)