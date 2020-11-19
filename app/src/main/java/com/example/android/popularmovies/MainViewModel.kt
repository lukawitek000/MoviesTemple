package com.example.android.popularmovies

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.android.popularmovies.models.Movie
import com.example.android.popularmovies.models.MovieWithReviewsAndVideos
import kotlinx.coroutines.*
import java.lang.Exception

class MainViewModel(application: Application) : ViewModel() {

    enum class Status {
        LOADING, SUCCESS, FAILURE
    }

    private val repository = MainRepository(application)

    private val _popularMovies = MutableLiveData<List<Movie>>()
    val popularMovies: LiveData<List<Movie>>
        get() = _popularMovies



    private val _topRatedMovies = MutableLiveData<List<Movie>>()
    val topRatedMovies: LiveData<List<Movie>>
        get() = _topRatedMovies


    private val _initialApiRequestStatus = MutableLiveData<Status>()
    val initialApiRequestStatus: LiveData<Status>
        get() = _initialApiRequestStatus


    val databaseValues  = repository.favouriteMovies

    private var favouriteMovies: List<Movie> = emptyList()
    fun getFavouriteMovies(): List<Movie>{
        return favouriteMovies
    }


    private val _recommendedMovies = MutableLiveData<Set<Movie>>()
    val recommendedMovies: LiveData<Set<Movie>>
    get() = _recommendedMovies

    private val _selectedMovie = MutableLiveData<Movie>()
    val selectedMovie: LiveData<Movie>
        get() = _selectedMovie


    fun selectMovie(movie: Movie) {
        _selectedMovie.value = movie
        getDetailInformation()
    }


    fun getMovies(){
        viewModelScope.launch {
            try {
                val startTime = System.currentTimeMillis()
                _initialApiRequestStatus.value = Status.LOADING
                _popularMovies.value = repository.getPopularMovies()
                Log.i("MainViewModel", "time elapsed for fetching data after popular = ${System.currentTimeMillis() - startTime}")
                _topRatedMovies.value = repository.getTopRatedMovies()
                _initialApiRequestStatus.value = Status.SUCCESS
                Log.i("MainViewModel", "time elapsed for fetching data end = ${System.currentTimeMillis() - startTime}")
            } catch (e: Exception) {
                Log.i("MainViewModel", "failure e=$e")
                _initialApiRequestStatus.value = Status.FAILURE
            }
        }
    }


    fun addMovieToDatabase() {
        viewModelScope.launch {
            repository.insertMovieToDatabase(_selectedMovie.value!!)
        }
    }

    fun setResponseFromDatabaseToFavouriteMovies(response: List<MovieWithReviewsAndVideos>) {
        val movies = mutableListOf<Movie>()
        for(value in response){
            val movie = value.movie
            movie.videos = value.videos
            movie.reviews = value.reviews
            movies.add(movie)
        }
        favouriteMovies = movies
    }


    fun deleteMovieFromDatabase(){
        viewModelScope.launch {
            val movieToDelete = favouriteMovies.find {
                it.id == _selectedMovie.value!!.id
            }
            repository.deleteMovieFromDatabase(movieToDelete!!)
        }
    }

    fun isSelectedMovieInDatabase(): Boolean{
        favouriteMovies.forEach {
            if(it.id == _selectedMovie.value!!.id){
                return true
            }
        }
        return false
    }


    private val _requestDetailInformationStatus = MutableLiveData<Status>()
    val requestDetailInformationStatus: LiveData<Status>
    get() = _requestDetailInformationStatus


    private fun getDetailInformation(){
        if(isSelectedMovieInDatabase()){
            getDetailInformationFromDatabase()
        }else{
            requestApiForDetailInformation()
        }
    }

    private fun getDetailInformationFromDatabase() {
        _requestDetailInformationStatus.value = Status.SUCCESS
    }


    private fun requestApiForDetailInformation(){
        viewModelScope.launch {
            try{
                val start = System.currentTimeMillis()
                _requestDetailInformationStatus.value = Status.LOADING
                _selectedMovie.value = repository.getMovieDetails(_selectedMovie.value!!)
                _requestDetailInformationStatus.value = Status.SUCCESS
                Log.i("MainViewModel", "time elaspsed ${System.currentTimeMillis() - start}")
            }catch (e: Exception){
                Log.i("MainViewModel", "errror $e")
                _requestDetailInformationStatus.value = Status.FAILURE
            }
        }
    }


    fun getRecommendationsBasedOnFavouriteMovies(){
        viewModelScope.launch {
            if(_initialApiRequestStatus.value == Status.SUCCESS) {
                val recommendationsList = mutableSetOf<Movie>()
                val recommendations = favouriteMovies.map {
                    async {
                        recommendationsList.addAll(repository.getRecommendationBasedOnMovieID(it.id))
                    }
                }
                recommendations.awaitAll()
                _recommendedMovies.value = recommendationsList
            }
        }
    }

}