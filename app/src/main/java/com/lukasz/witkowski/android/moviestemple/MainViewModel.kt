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

    private val repository = MainRepository.getInstance(application)


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