package com.lukasz.witkowski.android.moviestemple.api

import com.lukasz.witkowski.android.moviestemple.models.responses.MovieDetailsResponse
import com.lukasz.witkowski.android.moviestemple.models.responses.TMDBResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query






const val IMAGE_WIDTH = 500
const val URL_ADDRESS = "https://api.themoviedb.org"
const val API_KEY = "api_key"
const val api_key = "3b623a17f57eb4da612b3871d3f78ced"
const val POSTER_BASE_URI = "http://image.tmdb.org/t/p/w500"
const val VIDEO_BASE_URI = "https://www.youtube.com/watch?v="
const val PERSON_BASE_URI = "https://www.themoviedb.org/person/"
const val append_to_response = "videos,reviews,credits"
const val APPEND_TO_RESPONSE = "append_to_response"

const val TMDB_STARTING_PAGE_INDEX = 1
const val TMDB_PAGE_SIZE = 20


const val POPULAR_MOVIES_QUERY = "popular_movies_query"
const val TOP_RATED_MOVIES_QUERY = "top_rated_movies_query"
const val RECOMMENDATIONS_QUERY = "recommendations_query"


/*
val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(URL_ADDRESS)
        .build()

object TMDBApi{
    val retrofitService : TMDBService by lazy {
        retrofit.create(TMDBService::class.java)
    }
}
*/
