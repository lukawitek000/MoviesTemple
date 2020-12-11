package com.lukasz.witkowski.android.moviestemple.di

import com.lukasz.witkowski.android.moviestemple.api.TMDBService
import com.lukasz.witkowski.android.moviestemple.api.URL_ADDRESS
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
object TMDBServiceModule {


    @Singleton
    @Provides
    fun provideMoshi(): Moshi{
        return Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    }


    @Singleton
    @Provides
    fun provideRetrofit(moshi: Moshi): Retrofit.Builder{
        return Retrofit.Builder()
                .baseUrl(URL_ADDRESS)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
    }


    @Singleton
    @Provides
    fun provideTMDBService(retrofit: Retrofit.Builder): TMDBService {
        return retrofit.build().create(TMDBService::class.java)
    }


}