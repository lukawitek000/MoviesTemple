package com.lukasz.witkowski.android.moviestemple.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lukasz.witkowski.android.moviestemple.database.entities.*


@Database(entities = [MovieEntity::class, ReviewEntity::class, VideoEntity::class, GenreEntity::class,
    MovieWithGenre::class, ActorEntity::class, MovieWithActor::class, DirectorEntity::class,
    MovieWithDirector::class, WriterEntity::class, MovieWithWriter::class],
        version = 20, exportSchema = false)
abstract class FavouriteMovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao

    companion object {
        const val DATABASE_NAME = "FAVOURITE_MOVIES"
    }
}