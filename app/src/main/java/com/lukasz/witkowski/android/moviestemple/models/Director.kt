package com.lukasz.witkowski.android.moviestemple.models


data class Director(
        var directorId: Int = 0,
        override var name: String = "",
        var profilePath: String? = null,
): NameInterface