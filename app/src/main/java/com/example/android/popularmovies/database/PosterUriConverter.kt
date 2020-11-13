package com.example.android.popularmovies.database

import android.net.Uri
import androidx.room.TypeConverter

class PosterUriConverter {
    @TypeConverter
    fun toString(uri: Uri): String {
        return uri.toString()
    }

    @TypeConverter
    fun toUri(str: String?): Uri {
        return Uri.parse(str)
    }
}