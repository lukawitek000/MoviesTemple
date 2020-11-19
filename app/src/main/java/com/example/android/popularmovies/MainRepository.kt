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
            //getVideosAndReviews(movieInfoResponse)

            getMoviesDetails(movieInfoResponse)
            movieInfoResponse.movies
        }
    }

    private suspend fun getMoviesDetails(movieInfoResponse: TMDBResponse) {
        CoroutineScope(IO).launch {
            val allResponses = movieInfoResponse.movies.map {
                async {
                    val response = TMDBApi.retrofitService.getMovieDetailsVideosReviewsById(it.id)
                    it.reviews = response.reviews.results
                    it.videos = response.videos.results
                }
            }
            allResponses.awaitAll()
        }


    }


    private suspend fun getVideosAndReviews(movieInfoResponse: TMDBResponse) {
        CoroutineScope(IO).launch {
            val fetchReviews = movieInfoResponse.movies.map {
                async {
                    it.reviews = TMDBApi.retrofitService.getReviews(it.id).results
                }
            }

            val fetchVideos = movieInfoResponse.movies.map {
                async {
                    it.videos = TMDBApi.retrofitService.getVideos(it.id).results
                }
            }

            fetchReviews.awaitAll()
            fetchVideos.awaitAll()
        }

    }


    suspend fun getTopRatedMovies(): List<Movie>{
        return withContext(IO){
            val response = TMDBApi.retrofitService.getTopRatedMovies()
           // getVideosAndReviews(response)
            getMoviesDetails(response)
            response.movies
        }
    }

    suspend fun getRecommendationBasedOnMovieID(movieID: Long): List<Movie> {
        return withContext(IO){
            val response = TMDBApi.retrofitService.getRecommendationsBaseOnMovieID(movieID)
            getVideosAndReviews(response)
            //getMoviesDetails(response)
            response.movies
        }
    }

}