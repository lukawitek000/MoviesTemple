package com.example.android.popularmovies.database

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.android.popularmovies.models.Review

@Entity(tableName = "Favorite_Movies")
data class MovieEntity(
    @PrimaryKey(autoGenerate = true)
    var databaseID: Long = 0,
    var id: Int = 0,
    @ColumnInfo(name = "original_title")
    var originalTitle: String? = null,
    var title: String? = null,

    @ColumnInfo(name = "poster_uri")
    var posterUri: Uri? = null,
    var overview: String? = null,

    @ColumnInfo(name = "vote_average")
    var voteAverage: Float = 0f,

    @ColumnInfo(name = "release_date")
    var releaseDate: String? = null,

    @ColumnInfo(name = "video_urls")
    var videoUrls: Array<String>,
    var reviews: Array<Review>
)