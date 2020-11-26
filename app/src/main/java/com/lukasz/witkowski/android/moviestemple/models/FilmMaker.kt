package com.lukasz.witkowski.android.moviestemple.models



data class FilmMaker(
        var id: Int = 0,
        var name: String = "",
        var profilePath: String? = null,
        var character: String = "",
        var job: String = "Acting"
)