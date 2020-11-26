package com.lukasz.witkowski.android.moviestemple.models.responses

import com.squareup.moshi.Json

data class Actor(
        var adult: Boolean = false,
        var gender: Int? = null,
        var id: Int = 0,
        @Json(name = "known_for_department")
        var knownForDepartment: String = "",
        var name: String = "",
        @Json(name = "original_name")
        var originalName: String = "",
        var popularity: Float = 0f,
        @Json(name = "profile_path")
        var profilePath: String? = null,
        @Json(name = "cast_id")
        var castId: Int = 0,
        var character: String = "",
        @Json(name = "credit_id")
        var creditId: String = "",
        var order: Int = 0
)