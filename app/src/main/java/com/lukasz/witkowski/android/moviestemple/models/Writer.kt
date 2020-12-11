package com.lukasz.witkowski.android.moviestemple.models



data class Writer (
        var writerId: Int = 0,
        override var name: String = "",
        var profilePath: String? = null,
): NameInterface