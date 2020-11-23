package com.lukasz.witkowski.android.moviestemple

import android.app.Application
import com.lukasz.witkowski.android.moviestemple.database.FavouriteMovieDatabase
import com.lukasz.witkowski.android.moviestemple.models.*
import com.lukasz.witkowski.android.moviestemple.utilities.TMDBApi
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
            //delay(5000)
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
            //delay(10000)
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


    suspend fun deleteAllFavouriteMovies(){
        withContext(IO){
            database?.movieDao()?.deleteAll()
        }
    }

}