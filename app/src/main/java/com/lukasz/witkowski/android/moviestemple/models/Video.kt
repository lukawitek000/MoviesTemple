package com.lukasz.witkowski.android.moviestemple.models

import android.net.Uri
import com.lukasz.witkowski.android.moviestemple.api.VIDEO_BASE_URI


data class Video(
        var key: String,
        var name: String,
        var site: String,
){
    val videoUri: Uri
    get() = Uri.parse(VIDEO_BASE_URI + key)
}