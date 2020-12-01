package com.lukasz.witkowski.android.moviestemple.models

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Directors")
data class Director(
        @PrimaryKey
        var directorId: Int = 0,
        var name: String = "",
        var profilePath: String? = null,
)