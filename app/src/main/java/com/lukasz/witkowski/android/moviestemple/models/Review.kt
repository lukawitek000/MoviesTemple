package com.lukasz.witkowski.android.moviestemple.models

import androidx.room.Entity
import androidx.room.PrimaryKey


data class Review(
        var author: String = "",
        var content: String = "",
        )