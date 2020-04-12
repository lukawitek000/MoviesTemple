package com.example.android.popularmovies.database;

import android.net.Uri;

import androidx.room.TypeConverter;

class PosterUriConverter {

    @TypeConverter
    public static String toString(Uri uri){
        return uri.toString();
    }

    @TypeConverter
    public static Uri toUri(String str){
        return Uri.parse(str);
    }

}
