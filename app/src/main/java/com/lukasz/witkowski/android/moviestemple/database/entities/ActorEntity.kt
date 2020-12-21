package com.lukasz.witkowski.android.moviestemple.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Actors")
data class ActorEntity(
        @PrimaryKey
        var actorId: Int = 0,
        var name: String = "",
        var profilePath: String? = null,
        var character: String = "",
        var order: Int = 0
)