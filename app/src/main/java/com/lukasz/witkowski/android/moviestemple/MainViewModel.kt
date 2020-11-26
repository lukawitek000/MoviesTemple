package com.lukasz.witkowski.android.moviestemple

import android.app.Application
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.lukasz.witkowski.android.moviestemple.models.Movie
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow

class MainViewModel(application: Application) : ViewModel() {

    enum class Status {
        LOADING, SUCCESS, FAILURE
    }

    private val repository = MainRepository.getInstance(application)

    val favouriteMovies = repository.favouriteMovies.cachedIn(viewModelScope)

    private val _selectedMovie = MutableLiveData<Movie>()
    val selectedMovie: LiveData<Movie>
        get() = _selectedMovie

    private val _requestDetailInformationStatus = MutableLiveData<Status>()
    val requestDetailInformationStatus: LiveData<Status>
        get() = _requestDetailInformationStatus


    fun selectMovie(movie: Movie) {
        _selectedMovie.value = movie
        isSelectedMovieInDatabase()
        getDetailInformation()
    }


    fun addMovieToDatabase() {
        viewModelScope.launch {
            repository.insertMovieToDatabase(_selectedMovie.value!!)
        }
    }



    fun deleteMovieFromDatabase(){
        viewModelScope.launch {
            repository.deleteMovieFromDatabase(_selectedMovie.value!!)
        }
    }



    private fun getDetailInformation(){
        viewModelScope.launch {
            try {
                _requestDetailInformationStatus.value = Status.LOADING
                repository.getDetailInformation(_selectedMovie.value!!)
                _requestDetailInformationStatus.value = Status.SUCCESS
            }catch (e: Exception){
                _requestDetailInformationStatus.value = Status.FAILURE
            }

        }
    }



    fun isSelectedMovieInDatabase(): Boolean{
        return repository.isMovieInFavourites(_selectedMovie.value!!.id)
    }


    fun deleteAllFavouriteMovies(){
        viewModelScope.launch {
            repository.deleteAllFavouriteMovies()
        }
    }



}