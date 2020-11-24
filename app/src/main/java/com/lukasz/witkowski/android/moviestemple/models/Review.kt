package com.lukasz.witkowski.android.moviestemple.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Review(
        @PrimaryKey(autoGenerate = true)
        var reviewID: Long = 0L,
        var movieOwnerID: Long = 0L,
        var author: String = "",
        var content: String = "",
        var id: String,
        var url: String
        ) {
        constructor(): this(0L, 0L, "", "", "", "")
}