package com.example.android.popularmovies

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.android.popularmovies.database.FavouriteMovieDatabase
import com.example.android.popularmovies.database.MovieEntity

class DetailInformationViewModel(database: FavouriteMovieDatabase, movieId: Int) : ViewModel() {
    val movie: LiveData<MovieEntity?>? = database.movieDao()!!.loadMovieById(movieId)

}