package com.lukasz.witkowski.android.moviestemple.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "Genres")
data class Genre(
        @Json(name = "id")
        @PrimaryKey
        var genreId: Int = 0,
        override var name: String = ""
): NameInterface