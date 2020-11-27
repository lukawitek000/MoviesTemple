package com.lukasz.witkowski.android.moviestemple.models

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lukasz.witkowski.android.moviestemple.api.POSTER_BASE_URI

@Entity(tableName = "Actors")
data class Actor(
        @PrimaryKey
        var actorId: Int = 0,
        var name: String = "",
        var profilePath: String? = null,
        var character: String = "",
        var order: Int = 0
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