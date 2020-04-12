package com.example.android.popularmovies.database;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {MovieEntity.class}, version = 5, exportSchema = false)
@TypeConverters({ReviewsConverter.class,  VideoUrlsConverter.class, PosterUriConverter.class})
public abstract class FavouriteMovieDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "FAVOURITE_MOVIES";
    private static final Object LOCK = new Object();
    private static FavouriteMovieDatabase instance;

    public static FavouriteMovieDatabase getInstance(Context context){
        if(instance == null){
            synchronized (LOCK){
                instance = Room.databaseBuilder(context.getApplicationContext(),
                        FavouriteMovieDatabase.class, FavouriteMovieDatabase.DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .build();
            }
        }
        return instance;
    }

    public abstract MovieDao movieDao();

}
