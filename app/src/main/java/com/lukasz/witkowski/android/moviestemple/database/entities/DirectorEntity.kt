package com.lukasz.witkowski.android.moviestemple.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lukasz.witkowski.android.moviestemple.models.NameInterface

@Entity(tableName = "Directors")
data class DirectorEntity(
        @PrimaryKey
        var directorId: Int = 0,
        override var name: String = "",
        var profilePath: String? = null,
): NameInterface