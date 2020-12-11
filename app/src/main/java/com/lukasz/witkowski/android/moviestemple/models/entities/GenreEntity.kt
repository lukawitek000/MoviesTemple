package com.lukasz.witkowski.android.moviestemple.models.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lukasz.witkowski.android.moviestemple.models.NameInterface
import com.squareup.moshi.Json

@Entity(tableName = "Genres")
data class GenreEntity(
        @Json(name = "id")
        @PrimaryKey
        var genreId: Int = 0,
        override var name: String = ""
): NameInterface