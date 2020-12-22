package com.lukasz.witkowski.android.moviestemple.api.responses

data class ReviewResponse(
        var author: String = "",
        var content: String = "",
        var id: String,
        var url: String
)