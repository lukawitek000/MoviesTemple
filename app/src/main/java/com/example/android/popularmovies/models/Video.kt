package com.example.android.popularmovies.models

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.android.popularmovies.utilities.TRAILER_BASE_URI
import com.squareup.moshi.Json


@Entity
data class Video(
        @PrimaryKey(autoGenerate = true)
        var videoID: Long = 0L,
        var movieOwnerID: Long = 0L,
        var id: String,
        @Json(name = "iso_639_1")
        var iso6391: String,
        @Json(name = "iso_3166_1")
        var iso31661: String,
        var key: String,
        var name: String,
        var site: String,
        var size: Int,
        var type: String = "Trailer"
){
        val videoUri: Uri
        get() = Uri.parse(TRAILER_BASE_URI + key)
        constructor():this(0L, 0L, "", "", "", "",
                "", "", 0, "")
}