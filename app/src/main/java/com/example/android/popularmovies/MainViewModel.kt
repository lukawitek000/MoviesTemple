package com.example.android.popularmovies

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.android.popularmovies.database.FavouriteMovieDatabase.Companion.getInstance
import com.example.android.popularmovies.database.MovieEntity

class MainViewModel(application: Application) : ViewModel() {
    val favouriteMovies: LiveData<List<MovieEntity?>?>?

    init {
        val database = getInstance(application)
        favouriteMovies = database!!.movieDao()!!.loadAllMovies()
    }
}