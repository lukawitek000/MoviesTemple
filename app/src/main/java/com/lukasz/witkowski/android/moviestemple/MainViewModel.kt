package com.lukasz.witkowski.android.moviestemple

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import androidx.paging.PagingData
import com.lukasz.witkowski.android.moviestemple.models.Movie
import com.lukasz.witkowski.android.moviestemple.models.entities.MovieWithReviewsAndVideos
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import java.lang.Exception

class MainViewModel(application: Application) : ViewModel() {

    enum class Status {
        LOADING, SUCCESS, FAILURE
    }

    private val repository = MainRepository.getInstance(application)

    val databaseValues: LiveData<List<Movie>>  = repository.favouriteMovies


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
        Log.i("MainViewModel", "database values ${databaseValues.value} selected item ${_selectedMovie.value}")
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
        _selectedMovie.value = databaseValues.value?.find {
            it.id == _selectedMovie.value?.id
        }
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


    fun deleteAllFavouriteMovies(){
        viewModelScope.launch {
            repository.deleteAllFavouriteMovies()
        }
    }

    fun getRecommendationsBasedOnFavouriteMovies(): Flow<PagingData<Movie>> {
        Log.i("RecommendedMoviesModel", "favouriteMovies = ${databaseValues.value}")
        return repository.getRecommendationsBasedOnFavouriteMovies(databaseValues.value!!)
    }

}