package com.lukasz.witkowski.android.moviestemple.models

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Writers")
data class Writer (
    @PrimaryKey
    var writerId: Int = 0,
    var name: String = "",
    var profilePath: String? = null,
)