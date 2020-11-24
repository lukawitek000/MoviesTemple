package com.lukasz.witkowski.android.moviestemple

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.lukasz.witkowski.android.moviestemple.models.Movie
import com.lukasz.witkowski.android.moviestemple.models.entities.MovieWithReviewsAndVideos
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

    private val _topRatedMoviesStatus = MutableLiveData<Status>()
    val topRatedMoviesStatus: LiveData<Status>
        get() = _topRatedMoviesStatus


    private val _popularMoviesStatus = MutableLiveData<Status>()
    val popularMoviesStatus: LiveData<Status>
        get() = _popularMoviesStatus


    val databaseValues: LiveData<List<Movie>>  = repository.favouriteMovies


    private val _recommendedMovies = MutableLiveData<Set<Movie>>()
    val recommendedMovies: LiveData<Set<Movie>>
    get() = _recommendedMovies


    private val _recommendedMoviesStatus = MutableLiveData<Status>()
    val recommendedMoviesStatus: LiveData<Status>
        get() = _recommendedMoviesStatus

    private val _selectedMovie = MutableLiveData<Movie>()
    val selectedMovie: LiveData<Movie>
        get() = _selectedMovie

    private val _requestDetailInformationStatus = MutableLiveData<Status>()
    val requestDetailInformationStatus: LiveData<Status>
        get() = _requestDetailInformationStatus


    fun selectMovie(movie: Movie) {
        _selectedMovie.value = movie
        getDetailInformation()
    }




    fun getPopularMovies(){
        viewModelScope.launch {
            try {
                val startTime = System.currentTimeMillis()
                _popularMoviesStatus.value = Status.LOADING
                _popularMovies.value = repository.getPopularMovies()
                _popularMoviesStatus.value = Status.SUCCESS
                Log.i("MainViewModel", "time elapsed popular for fetching data end = ${System.currentTimeMillis() - startTime}")
            } catch (e: Exception) {
                Log.i("MainViewModel", "failure e=$e")
                _popularMoviesStatus.value = Status.FAILURE
            }
        }
    }


    fun getTopRatedMovies(){
        viewModelScope.launch {
            try {
                val startTime = System.currentTimeMillis()
                _topRatedMoviesStatus.value = Status.LOADING
                _topRatedMovies.value = repository.getTopRatedMovies()
                _topRatedMoviesStatus.value = Status.SUCCESS
                Log.i("MainViewModel", "time elapsed toprated for fetching data end = ${System.currentTimeMillis() - startTime}")
            } catch (e: Exception) {
                Log.i("MainViewModel", "failure e=$e")
                _topRatedMoviesStatus.value = Status.FAILURE
            }
        }
    }


    fun addMovieToDatabase() {
        viewModelScope.launch {
            repository.insertMovieToDatabase(_selectedMovie.value!!)
        }
    }



    fun deleteMovieFromDatabase(){
        viewModelScope.launch {
            val movieToDelete = databaseValues.value?.find {
                it.id == _selectedMovie.value!!.id
            }
            repository.deleteMovieFromDatabase(movieToDelete!!)
        }
    }

    fun isSelectedMovieInDatabase(): Boolean{
        databaseValues.value?.forEach {
            if(it.id == _selectedMovie.value!!.id){
                return true
            }
        }
        return false
    }





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
            try {
                _recommendedMoviesStatus.value = Status.LOADING
                val recommendationsList = mutableSetOf<Movie>()
                for(movie in databaseValues.value!!){
                    val response = repository.getRecommendationBasedOnMovieID(movie.id)
                    recommendationsList.addAll(response)
                }
                _recommendedMovies.value = recommendationsList.shuffled().toSet()
                _recommendedMoviesStatus.value = Status.SUCCESS
            }catch (e: Exception){
                Log.i("MainViewModel", "Error with fetching recommendations")
                _recommendedMoviesStatus.value = Status.FAILURE
            }
        }
    }



    fun deleteAllFavouriteMovies(){
        viewModelScope.launch {
            repository.deleteAllFavouriteMovies()
        }
    }

}