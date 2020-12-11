package com.lukasz.witkowski.android.moviestemple.api

import com.lukasz.witkowski.android.moviestemple.models.responses.MovieDetailsResponse
import com.lukasz.witkowski.android.moviestemple.models.responses.TMDBResponse
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
    suspend fun getMovieDetailsVideosReviewsById(@Path("movieId") movieId: Int,
                                                 @Query(APPEND_TO_RESPONSE) appendToResponse: String = append_to_response,
                                                 @Query(API_KEY) apiKey: String = api_key): MovieDetailsResponse

    @GET("/3/search/movie")
    suspend fun getSearchMovies(
            @Query(API_KEY) apiKey: String = api_key,
            @Query("query") query: String,
            @Query("page") page: Int = 1
    ): TMDBResponse


}