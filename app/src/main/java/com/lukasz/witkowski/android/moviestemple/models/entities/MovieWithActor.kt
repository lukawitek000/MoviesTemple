package com.lukasz.witkowski.android.moviestemple.models.entities

import androidx.room.Entity


@Entity(primaryKeys = ["movieId", "actorId"])
data class MovieWithActor(
        val movieId: Int,
        val actorId: Int
)