package com.example.android.popularmovies

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.android.popularmovies.models.Movie
import com.example.android.popularmovies.models.Review
import com.example.android.popularmovies.models.TMDBResponse
import com.example.android.popularmovies.models.Video
import com.example.android.popularmovies.utilities.TMDBApi
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class MainRepository(private val application: Application) {

    //private val database

    private val popularMovies = MutableLiveData<List<Movie>>()

    suspend fun getPopularMovies(): List<Movie>{
        return withContext(Dispatchers.IO){
           /*popularMovies.value = TMDBApi.retrofitService.getPopularMovies()
            popularMovies.value!!*/
            //val response = TMDBApi.retrofitService.getPopularMovies()
            //response.movies

            val movieInfoResponse = TMDBApi.retrofitService.getPopularMovies()
            getVideosAndReviews(movieInfoResponse)
            movieInfoResponse.movies
        }
    }

    private suspend fun getVideosAndReviews(movieInfoResponse: TMDBResponse) {
        for (movie in movieInfoResponse.movies) {
            val trailers: Deferred<List<Video>> = getVideosAsync(movie)
            val reviews: Deferred<List<Review>> =  getReviewsAsync(movie)
            movie.trailers = trailers.await()
            movie.reviews = reviews.await()
        }

    }

    private suspend fun getReviewsAsync(movie: Movie): Deferred<List<Review>> {
        return withContext(Dispatchers.IO) {
            async {
                val reviewsResponse = TMDBApi.retrofitService.getReviews(movie.id)
                reviewsResponse.results
            }
        }
    }

    private suspend fun getVideosAsync(movie: Movie): Deferred<List<Video>> {
        return withContext(Dispatchers.IO) {
            async {
                val trailersResponse = TMDBApi.retrofitService.getVideos(movie.id)
                trailersResponse.results
            }
        }
    }


    suspend fun getTopRatedMovies(): List<Movie>{
        return withContext(Dispatchers.IO){
            val response = TMDBApi.retrofitService.getTopRatedMovies()
            response.movies
        }
    }

}