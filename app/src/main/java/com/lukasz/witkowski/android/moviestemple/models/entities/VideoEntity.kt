package com.lukasz.witkowski.android.moviestemple.models.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json


@Entity(tableName = "Videos")
data class VideoEntity(
        @PrimaryKey(autoGenerate = true)
        var videoID: Long = 0L,
        var movieOwnerID: Long = 0L,
        var key: String,
        var name: String,
        var site: String,
)