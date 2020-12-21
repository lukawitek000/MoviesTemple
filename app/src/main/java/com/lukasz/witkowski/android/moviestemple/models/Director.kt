package com.lukasz.witkowski.android.moviestemple.models

import androidx.room.Entity
import androidx.room.PrimaryKey


data class Director(
        var directorId: Int = 0,
        override var name: String = "",
        var profilePath: String? = null,
): NameInterface