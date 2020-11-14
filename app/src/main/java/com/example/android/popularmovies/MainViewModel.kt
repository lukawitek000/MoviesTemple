package com.example.android.popularmovies

import android.app.Application
import androidx.lifecycle.*
import com.example.android.popularmovies.database.FavouriteMovieDatabase.Companion.getInstance
import com.example.android.popularmovies.database.MovieEntity
import com.example.android.popularmovies.models.Movie
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : ViewModel() {
    //val favouriteMovies: LiveData<List<MovieEntity?>?>?


    private val _popularMovies = MutableLiveData<List<Movie>>()

    private val repository = MainRepository(application)

    val popularMovies: LiveData<List<Movie>>
        get() = _popularMovies


    init {
        //val database = getInstance(application)
       // favouriteMovies = database!!.movieDao()!!.loadAllMovies()
    }

    fun getPopularMovies(){
        viewModelScope.launch {
            _popularMovies.value = repository.getPopularMovies()
        }
    }


}