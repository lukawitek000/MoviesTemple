package com.example.android.popularmovies

import android.app.Application
import com.example.android.popularmovies.database.FavouriteMovieDatabase
import com.example.android.popularmovies.models.*
import com.example.android.popularmovies.utilities.TMDBApi
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO

class MainRepository(application: Application) {

    private val database = FavouriteMovieDatabase.getInstance(application.applicationContext)

    val favouriteMovies = database!!.movieDao().loadAllMovies()

    suspend fun deleteMovieFromDatabase(movie: Movie){
        withContext(IO){
            database!!.movieDao().deleteMovieReviewAndVideo(movie)
        }
    }


    suspend fun insertMovieToDatabase(movie: Movie){
        withContext(IO){
            database?.movieDao()?.insert(movie)
        }
    }


    suspend fun getPopularMovies(): List<Movie>{
        return withContext(IO){
            delay(1000)
            val movieInfoResponse = TMDBApi.retrofitService.getPopularMovies()
            movieInfoResponse.movies
        }
    }



    suspend fun getMovieDetails(movie: Movie): Movie {
        return (IO) {
           // delay(10000)
            val response = TMDBApi.retrofitService.getMovieDetailsVideosReviewsById(movie.id)
            movie.reviews  = response.reviews.results
            movie.videos = response.videos.results
            movie
        }
    }




    suspend fun getTopRatedMovies(): List<Movie>{
        return withContext(IO){
            val response = TMDBApi.retrofitService.getTopRatedMovies()
            response.movies
        }
    }

    suspend fun getRecommendationBasedOnMovieID(movieID: Long): List<Movie> {
        return withContext(IO){
            val response = TMDBApi.retrofitService.getRecommendationsBaseOnMovieID(movieID)
            response.movies
        }
    }

}