package com.example.android.popularmovies.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.android.popularmovies.models.Movie
import com.example.android.popularmovies.models.Review
import com.example.android.popularmovies.models.Video


@Database(entities = [Movie::class, Review::class, Video::class], version = 11, exportSchema = false)
abstract class FavouriteMovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao

    companion object {
        private const val DATABASE_NAME = "FAVOURITE_MOVIES"
        private val LOCK = Any()

        @Volatile
        private var instance: FavouriteMovieDatabase? = null

        fun getInstance(context: Context): FavouriteMovieDatabase? {
            if (instance == null) {
                synchronized(LOCK) {
                    instance = Room.databaseBuilder(context.applicationContext,
                            FavouriteMovieDatabase::class.java, DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .build()
                }
            }
            return instance
        }
    }
}