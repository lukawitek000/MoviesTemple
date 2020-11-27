package com.lukasz.witkowski.android.moviestemple.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lukasz.witkowski.android.moviestemple.models.*
import com.lukasz.witkowski.android.moviestemple.models.entities.*


@Database(entities = [MovieEntity::class, ReviewEntity::class, VideoEntity::class, Genre::class,
    MovieWithGenre::class, Actor::class, MovieWithActor::class],
        version = 18, exportSchema = false)
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