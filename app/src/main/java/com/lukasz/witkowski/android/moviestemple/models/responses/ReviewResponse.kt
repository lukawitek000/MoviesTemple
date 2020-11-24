package com.lukasz.witkowski.android.moviestemple.models.responses

import androidx.room.PrimaryKey

data class ReviewResponse(
        var author: String = "",
        var content: String = "",
        var id: String,
        var url: String
) {
}