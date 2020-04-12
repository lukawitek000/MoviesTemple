package com.example.android.popularmovies.database;

import androidx.room.TypeConverter;

class VideoUrlsConverter {

    @TypeConverter
    public static String toString(String[] arr){
        StringBuilder builder = new StringBuilder();
        for(String str : arr){
            builder.append(str).append("\n");
        }
        return  builder.toString();
    }

    @TypeConverter
    public static String[] toStringArray(String str){
        if(str.isEmpty()){
            return new String[0];
        }
        return str.split("\n");
    }


}
