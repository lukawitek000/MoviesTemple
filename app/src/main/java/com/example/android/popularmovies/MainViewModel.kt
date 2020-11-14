package com.example.android.popularmovies

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.android.popularmovies.database.FavouriteMovieDatabase.Companion.getInstance
import com.example.android.popularmovies.database.MovieEntity
import com.example.android.popularmovies.models.Movie
import kotlinx.coroutines.launch
import java.lang.Exception

class MainViewModel(application: Application) : ViewModel() {
    //val favouriteMovies: LiveData<List<MovieEntity?>?>?

    enum class MovieTypeList{
        POPULAR_MOVIES, TOP_RATED_MOVIES, FAVOURITE_MOVIES
    }

    enum class Status {
        LOADING, SUCCESS, FAILURE
    }



    private val repository = MainRepository(application)

    private lateinit var popularMovies: List<Movie>

    private lateinit var topRatedMovies: List<Movie>


    private var listType: MovieTypeList = MovieTypeList.POPULAR_MOVIES

    fun setListType(typeList: MovieTypeList){
        listType = typeList
        setMoviesList()
    }

    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>>
        get() = _movies


    private val _status = MutableLiveData<Status>()

    val status: LiveData<Status>
        get() = _status


    init {
        getMovies()
    }

    private suspend fun getPopularMovies(){
        popularMovies = repository.getPopularMovies()
        Log.i("MainViewModel", "getpopularmovies = $popularMovies")
    }

    private suspend fun getTopRatedMovies(){
        topRatedMovies = repository.getTopRatedMovies()
        Log.i("MainViewModel", "getTopratedMoveis = $topRatedMovies")
    }

    private fun getMovies(){
        viewModelScope.launch {
            try {
                _status.value = Status.LOADING
                getPopularMovies()
                getTopRatedMovies()
                Log.i("MainViewModel", "popularmovies = $popularMovies")
                setMoviesList()
                _status.value = Status.SUCCESS
            } catch (e: Exception) {
                Log.i("MainViewModel", "failure e=$e")
                _status.value = Status.FAILURE
            }
        }
    }

    private fun setMoviesList(){
        if(listType == MovieTypeList.POPULAR_MOVIES){
            _movies.value = popularMovies
        }else{
            _movies.value = topRatedMovies
        }
    }


}