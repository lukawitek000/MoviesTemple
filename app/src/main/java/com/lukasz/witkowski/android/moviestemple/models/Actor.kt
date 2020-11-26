package com.lukasz.witkowski.android.moviestemple.models

import android.net.Uri
import com.lukasz.witkowski.android.moviestemple.api.POSTER_BASE_URI


data class Actor(
        var id: Int = 0,
        var name: String = "",
        var profilePath: String? = null,
        var character: String = "",
        var job: String = "Acting"
){
    val actorPhoto : Uri?
    get() {
        return if(profilePath == null){
            null
        }else{
            Uri.parse(POSTER_BASE_URI + profilePath)
        }

    }
}