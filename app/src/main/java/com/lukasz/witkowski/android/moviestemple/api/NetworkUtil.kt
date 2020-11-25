package com.lukasz.witkowski.android.moviestemple.api

import com.lukasz.witkowski.android.moviestemple.models.responses.MovieDetailsResponse
import com.lukasz.witkowski.android.moviestemple.models.responses.ReviewsListResponse
import com.lukasz.witkowski.android.moviestemple.models.responses.TMDBResponse
import com.lukasz.witkowski.android.moviestemple.models.responses.VideosListResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface TMDBService{

    @GET("/3/movie/popular")
    suspend fun getPopularMovies(@Query(API_KEY) apiKey: String = api_key,
                                 @Query("page") page: Int = 1): TMDBResponse

    @GET("/3/movie/top_rated")
    suspend fun getTopRatedMovies(@Query(API_KEY) apiKey: String = api_key,
                                  @Query("page") page: Int = 1): TMDBResponse



    @GET("/3/movie/{movieId}/recommendations")
    suspend fun getRecommendationsBaseOnMovieID(
            @Path("movieId") movieId: Int,
            @Query(API_KEY) apiKey: String = api_key,
            @Query("page") page: Int = 1): TMDBResponse


    @GET("/3/movie/{movieId}")
    suspend fun getMovieDetailsVideosReviewsById( @Path("movieId") movieId: Int,
                                                  @Query(APPEND_TO_RESPONSE) appendToResponse: String = append_to_response,
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

const val TMDB_STARTING_PAGE_INDEX = 1
const val TMDB_PAGE_SIZE = 20

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

