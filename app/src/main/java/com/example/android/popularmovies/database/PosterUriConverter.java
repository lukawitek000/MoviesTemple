package com.example.android.popularmovies.database;

import android.net.Uri;

import androidx.room.TypeConverter;

public class PosterUriConverter {

    @TypeConverter
    public static String toString(Uri uri){
        return uri.toString();
    }

    @TypeConverter
    public static Uri toUri(String str){
        Uri uri = Uri.parse(str);
        return uri;
    }

}
