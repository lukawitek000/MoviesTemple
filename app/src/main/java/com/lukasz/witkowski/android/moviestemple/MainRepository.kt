package com.lukasz.witkowski.android.moviestemple

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.lukasz.witkowski.android.moviestemple.database.FavouriteMovieDatabase
import com.lukasz.witkowski.android.moviestemple.models.*
import com.lukasz.witkowski.android.moviestemple.utilities.TMDBApi
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO

class MainRepository(application: Application) {


    companion object {
        private val LOCK = Any()
        @Volatile
        private var instance: MainRepository? = null
        fun getInstance(application: Application): MainRepository {
            return instance ?: synchronized(LOCK){
                MainRepository(application)
            }
        }
    }

    init {
        Log.i("MainRepository", "init block")
    }


    private val database = FavouriteMovieDatabase.getInstance(application.applicationContext)

     val databaseResponse = database!!.movieDao().loadAllMovies()



    val favouriteMovies: LiveData<List<Movie>> = Transformations.map(databaseResponse){ responseList ->
        responseList.map {
            it.toMovie()
        }
    }

    suspend fun deleteMovieFromDatabase(movie: Movie){
        withContext(IO){
            database!!.movieDao().deleteMovieReviewAndVideo(movie.toMovieEntity())
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
            movieInfoResponse.movies.map {
                it.toMovie()
            }
        }
    }



    suspend fun getMovieDetails(movie: Movie): Movie {
        return (IO) {
           // delay(10000)
            val response = TMDBApi.retrofitService.getMovieDetailsVideosReviewsById(movie.id)
            movie.reviews  = response.reviews.results.map {
                it.toReview()
            }
            movie.videos = response.videos.results.map {
                it.toVideo()
            }
            movie
        }
    }




    suspend fun getTopRatedMovies(): List<Movie>{
        return withContext(IO){
            //delay(10000)
            val response = TMDBApi.retrofitService.getTopRatedMovies()
            response.movies.map {
                it.toMovie()
            }
        }
    }

    suspend fun getRecommendationBasedOnMovieID(movieID: Int): List<Movie> {
        return withContext(IO){
            val response = TMDBApi.retrofitService.getRecommendationsBaseOnMovieID(movieID)
            response.movies.map {
                it.toMovie()
            }
        }
    }


    suspend fun deleteAllFavouriteMovies(){
        withContext(IO){
            database?.movieDao()?.deleteAll()
        }
    }

}