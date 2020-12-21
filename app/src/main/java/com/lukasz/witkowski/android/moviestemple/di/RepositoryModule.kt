package com.lukasz.witkowski.android.moviestemple.di

import com.lukasz.witkowski.android.moviestemple.api.TMDBService
import com.lukasz.witkowski.android.moviestemple.database.MovieDao
import com.lukasz.witkowski.android.moviestemple.repository.MainRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
object RepositoryModule {


    @Singleton
    @Provides
    fun provideMainRepository(
            movieDao: MovieDao,
            tmdbService: TMDBService
    ): MainRepository{
        return MainRepository(movieDao, tmdbService)
    }

}