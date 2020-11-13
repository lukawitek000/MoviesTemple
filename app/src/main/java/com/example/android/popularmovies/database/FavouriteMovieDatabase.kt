package com.example.android.popularmovies.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [MovieEntity::class], version = 6, exportSchema = false)
@TypeConverters(ReviewsConverter::class, VideoUrlsConverter::class, PosterUriConverter::class)
abstract class FavouriteMovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao?

    companion object {
        private const val DATABASE_NAME = "FAVOURITE_MOVIES"
        private val LOCK = Any()
        private var instance: FavouriteMovieDatabase? = null
        @JvmStatic
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