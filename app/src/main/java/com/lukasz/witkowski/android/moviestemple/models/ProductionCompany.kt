package com.lukasz.witkowski.android.moviestemple.models

import com.squareup.moshi.Json

data class ProductionCompany(
        var name: String = "",
        var id: Int = 0,
        @Json(name = "logo_path")
        var logoPath: String? = null,
        @Json(name = "origin_country")
        var originCountry: String = ""
)