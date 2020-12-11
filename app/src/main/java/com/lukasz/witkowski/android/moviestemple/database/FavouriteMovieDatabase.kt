package com.lukasz.witkowski.android.moviestemple.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lukasz.witkowski.android.moviestemple.models.*
import com.lukasz.witkowski.android.moviestemple.models.entities.*


@Database(entities = [MovieEntity::class, ReviewEntity::class, VideoEntity::class, Genre::class,
    MovieWithGenre::class, Actor::class, MovieWithActor::class, Director::class,
    MovieWithDirector::class, Writer::class, MovieWithWriter::class],
        version = 20, exportSchema = false)
abstract class FavouriteMovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao

    companion object {
        const val DATABASE_NAME = "FAVOURITE_MOVIES"
    }
}