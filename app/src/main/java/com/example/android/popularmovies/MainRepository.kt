package com.example.android.popularmovies

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.popularmovies.models.Movie
import com.example.android.popularmovies.utilities.TMDBApi
import com.example.android.popularmovies.utilities.TMDBService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainRepository(private val application: Application) {

    //private val database

    private val popularMovies = MutableLiveData<List<Movie>>()

    suspend fun getPopularMovies(): List<Movie>{
        return withContext(Dispatchers.IO){
           /*popularMovies.value = TMDBApi.retrofitService.getPopularMovies()
            popularMovies.value!!*/
            val response = TMDBApi.retrofitService.getPopularMovies()
            response.movies
        }
    }

    suspend fun getTopRatedMovies(): List<Movie>{
        return withContext(Dispatchers.IO){
            val response = TMDBApi.retrofitService.getTopRatedMovies()
            response.movies
        }
    }

}