package com.lukasz.witkowski.android.moviestemple.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Reviews")
data class ReviewEntity(
        @PrimaryKey(autoGenerate = true)
        var reviewID: Long = 0L,
        var movieOwnerID: Long = 0L,
        var author: String = "",
        var content: String = "",
)