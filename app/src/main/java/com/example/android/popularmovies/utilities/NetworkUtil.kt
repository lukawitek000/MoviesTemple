package com.example.android.popularmovies.utilities

import android.net.Uri
import androidx.lifecycle.LiveData
import com.example.android.popularmovies.models.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.*


interface TMDBService{

    @GET("/3/movie/popular")
    suspend fun getPopularMovies(@Query(API_KEY) apiKey: String = api_key): TMDBResponse

    @GET("/3/movie/top_rated")
    suspend fun getTopRatedMovies(@Query(API_KEY) apiKey: String = api_key): TMDBResponse


    @GET("/3/movie/{movieId}/videos")
    suspend fun getVideos(
            @Path("movieId") movieId: Long,
            @Query(API_KEY) apiKey: String = api_key): VideoResponse

    @GET("/3/movie/{movieId}/reviews")
    suspend fun getReviews(
            @Path("movieId") movieId: Long,
            @Query(API_KEY) apiKey: String = api_key): ReviewResponse

    @GET("/3/movie/{movieId}/recommendations")
    suspend fun getRecommendationsBaseOnMovieID(
            @Path("movieId") movieId: Long,
            @Query(API_KEY) apiKey: String = api_key): TMDBResponse

    @GET("/3/movie/{movieId}")
    suspend fun getMovieDetailsVideosReviewsById( @Path("movieId") movieId: Long,
                                                  @Query("append_to_response") appendToResponse: String = append_to_response,
                                                  @Query(API_KEY) apiKey: String = api_key): MovieDetailsResponse

}



const val IMAGE_WIDTH = 500
private const val URL_ADDRESS = "https://api.themoviedb.org"
private const val API_KEY = "api_key"
private const val api_key = "3b623a17f57eb4da612b3871d3f78ced"
const val POSTER_BASE_URI = "http://image.tmdb.org/t/p/w500"
const val VIDEO_BASE_URI = "https://www.youtube.com/watch?v="
private const val append_to_response = "videos,reviews"
private const val APPEND_TO_RESPONSE = "append_to_response"


private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(URL_ADDRESS)
        .build()

object TMDBApi{
    val retrofitService : TMDBService by lazy {
        retrofit.create(TMDBService::class.java)
    }
}

