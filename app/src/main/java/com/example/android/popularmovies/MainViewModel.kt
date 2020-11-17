package com.example.android.popularmovies

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.android.popularmovies.models.Movie
import com.example.android.popularmovies.models.MovieWithReviewsAndVideos
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
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


    val databaseValues  = repository.favouriteMovies
    private lateinit var favouriteMovies: List<Movie>

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

    var selectedMovie: Movie? = null


    init {
        getMovies()
    }

    private fun getMovies(){
        viewModelScope.launch {
            try {
                val startTime = System.currentTimeMillis()
                _status.value = Status.LOADING
                getAllMovies()
                Log.i("MainViewModel", "popularmovies = $popularMovies")
                setMoviesList()
                _status.value = Status.SUCCESS
                Log.i("MainViewModel", "time elapsed for fetching data = ${System.currentTimeMillis() - startTime}")
            } catch (e: Exception) {
                Log.i("MainViewModel", "failure e=$e")
                _status.value = Status.FAILURE
            }
        }
    }

    private suspend fun getAllMovies() {
        withContext(IO) {
            val popularMovies: Deferred<List<Movie>> = async {
                repository.getPopularMovies()
            }
            val topRatedMovies: Deferred<List<Movie>> = async {
                repository.getTopRatedMovies()
            }
            this@MainViewModel.popularMovies = popularMovies.await()
            this@MainViewModel.topRatedMovies = topRatedMovies.await()
        }
    }

    private fun setMoviesList(){
        if(listType == MovieTypeList.POPULAR_MOVIES){
            _movies.value = popularMovies
        }else if(listType == MovieTypeList.TOP_RATED_MOVIES){
            _movies.value = topRatedMovies
        }else{
            Log.i("MainViewModel", "favourite movies: $favouriteMovies")
            //_movies.value = favouriteMovies.value
            _movies.value = favouriteMovies
        }
    }

    fun addMovieToDatabase() {
        viewModelScope.launch {
            repository.insertMovieToDatabase(selectedMovie!!)
        }
    }

    fun setFavouriteMovies(response: List<MovieWithReviewsAndVideos>) {
        val movies = mutableListOf<Movie>()
        for(value in response){
            val movie = value.movie
            movie.trailers = value.videos
            movie.reviews = value.reviews
            movies.add(movie)
        }
        favouriteMovies = movies
    }

    fun deleteMovieFromDatabase(){
        viewModelScope.launch {
            val movieToDelete = favouriteMovies.find {
                it.id == selectedMovie!!.id
            }
            repository.deleteMovieFromDatabase(movieToDelete!!)
        }
    }

    fun isSelectedMovieInDatabase(): Boolean{
        favouriteMovies.forEach {
            if(it.id == selectedMovie!!.id){
                return true
            }
        }
        return false
    }


}