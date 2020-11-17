package com.example.android.popularmovies

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.popularmovies.database.FavouriteMovieDatabase
import com.example.android.popularmovies.models.*
import com.example.android.popularmovies.utilities.TMDBApi
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO

class MainRepository(private val application: Application) {

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
            val movieInfoResponse = TMDBApi.retrofitService.getPopularMovies()
            getVideosAndReviews(movieInfoResponse)
            movieInfoResponse.movies
        }
    }

    private suspend fun getVideosAndReviews(movieInfoResponse: TMDBResponse) {
        CoroutineScope(IO).launch {
            val fetchReviews = movieInfoResponse.movies.map {
                async {
                    it.reviews = TMDBApi.retrofitService.getReviews(it.id).results
                }
            }

            val fetchTrailers = movieInfoResponse.movies.map {
                async {
                    it.trailers = TMDBApi.retrofitService.getVideos(it.id).results
                }
            }

            fetchReviews.awaitAll()
            fetchTrailers.awaitAll()
        }

    }



    suspend fun getTopRatedMovies(): List<Movie>{
        return withContext(IO){
            val response = TMDBApi.retrofitService.getTopRatedMovies()
            getVideosAndReviews(response)
            response.movies
        }
    }

}