package com.lukasz.witkowski.android.moviestemple.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ReviewEntity(
        @PrimaryKey(autoGenerate = true)
        var reviewID: Long = 0L,
        var movieOwnerID: Long = 0L,
        var author: String = "",
        var content: String = "",
) {
}