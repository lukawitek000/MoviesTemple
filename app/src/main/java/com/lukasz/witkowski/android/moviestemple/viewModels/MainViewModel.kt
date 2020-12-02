package com.lukasz.witkowski.android.moviestemple.viewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.lukasz.witkowski.android.moviestemple.MainRepository
import com.lukasz.witkowski.android.moviestemple.models.Movie
import com.lukasz.witkowski.android.moviestemple.models.entities.MovieEntity
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
                _selectedMovie.value = repository.getDetailInformation(_selectedMovie.value!!)
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


    private var currentQueryValue: String? = null
    private var currentSearchResult: Flow<PagingData<Movie>>? = null


    fun getMovies(query: String): Flow<PagingData<Movie>>{
        val lastResult = currentSearchResult
        if(query == currentQueryValue && lastResult != null){
            return lastResult
        }
        currentQueryValue = query
        val newResult = repository.getPagingDataMovies(query).cachedIn(viewModelScope)
        currentSearchResult = newResult
        return newResult
    }



    fun getRecommendationsBasedOnFavouriteMovies(): Flow<PagingData<Movie>> {
        return repository.getRecommendationsBasedOnFavouriteMovies()
    }

}