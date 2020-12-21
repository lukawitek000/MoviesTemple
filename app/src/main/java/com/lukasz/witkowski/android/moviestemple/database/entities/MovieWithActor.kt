package com.lukasz.witkowski.android.moviestemple.database.entities

import androidx.room.Entity


@Entity(primaryKeys = ["movieId", "actorId"])
data class MovieWithActor(
        val movieId: Int,
        val actorId: Int
)