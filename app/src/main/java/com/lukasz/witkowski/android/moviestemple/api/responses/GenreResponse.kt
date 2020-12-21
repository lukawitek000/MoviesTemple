package com.lukasz.witkowski.android.moviestemple.api.responses

import com.lukasz.witkowski.android.moviestemple.models.NameInterface
import com.squareup.moshi.Json

data class GenreResponse(
        @Json(name = "id")
        var genreId: Int = 0,
        override var name: String = ""
): NameInterface