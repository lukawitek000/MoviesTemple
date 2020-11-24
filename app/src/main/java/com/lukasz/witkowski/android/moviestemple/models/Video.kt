package com.lukasz.witkowski.android.moviestemple.models

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lukasz.witkowski.android.moviestemple.utilities.VIDEO_BASE_URI
import com.squareup.moshi.Json



data class Video(
        var key: String,
        var name: String,
        var site: String,
){
    val videoUri: Uri
    get() = Uri.parse(VIDEO_BASE_URI + key)
}