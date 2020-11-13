package com.example.android.popularmovies.database

import androidx.room.TypeConverter

class VideoUrlsConverter {

    @TypeConverter
    fun toString(arr: Array<String?>): String {
        val builder = StringBuilder()
        for (str in arr) {
            builder.append(str).append("\n")
        }
        return builder.toString()
    }

    @TypeConverter
    fun toStringArray(str: String): Array<String?> {
        return if (str.isEmpty()) {
            arrayOfNulls(0)
        } else str.split("\n").toTypedArray()
    }
}