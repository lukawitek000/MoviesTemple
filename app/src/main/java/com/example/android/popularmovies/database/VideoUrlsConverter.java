package com.example.android.popularmovies.database;

import androidx.room.TypeConverter;

public class VideoUrlsConverter {

    @TypeConverter
    public static String toString(String[] arr){
        StringBuilder builder = new StringBuilder();
        for(String str : arr){
            builder.append(str + "\n");
        }
        return  builder.toString();
    }

    @TypeConverter
    public static String[] toStringArray(String str){
        if(str.isEmpty() || str.equals("")){
            return new String[0];
        }
        return str.split("\n");
    }


}
