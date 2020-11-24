package com.lukasz.witkowski.android.moviestemple.models.responses

import com.squareup.moshi.Json

data class VideoResponse(
        var id: String,
        @Json(name = "iso_639_1")
        var iso6391: String,
        @Json(name = "iso_3166_1")
        var iso31661: String,
        var key: String,
        var name: String,
        var site: String,
        var size: Int,
        var type: String = ""
)