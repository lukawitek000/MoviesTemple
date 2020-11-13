package com.example.android.popularmovies

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.android.popularmovies.models.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainRepository(private val application: Application) {

    //private val database


    suspend fun getPopularMovies(): LiveData<Movie>{
        return withContext(Dispatchers.IO){

        }
    }

}