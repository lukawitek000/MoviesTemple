package com.lukasz.witkowski.android.moviestemple.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lukasz.witkowski.android.moviestemple.models.NameInterface

@Entity(tableName = "Writers")
data class WriterEntity (
        @PrimaryKey
        var writerId: Int = 0,
        override var name: String = "",
        var profilePath: String? = null,
): NameInterface