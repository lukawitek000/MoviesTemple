package com.lukasz.witkowski.android.moviestemple.di

import android.content.Context
import androidx.room.Room
import com.lukasz.witkowski.android.moviestemple.database.FavouriteMovieDatabase
import com.lukasz.witkowski.android.moviestemple.database.MovieDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun provideFavouriteMoviesDb(@ApplicationContext context: Context): FavouriteMovieDatabase{
        return Room.databaseBuilder(
                context,
                FavouriteMovieDatabase::class.java,
                FavouriteMovieDatabase.DATABASE_NAME
        )
                .fallbackToDestructiveMigration()
                .build()
    }

    @Singleton
    @Provides
    fun provideMovieDao(favouriteMovieDatabase: FavouriteMovieDatabase): MovieDao{
        return favouriteMovieDatabase.movieDao()
    }


}