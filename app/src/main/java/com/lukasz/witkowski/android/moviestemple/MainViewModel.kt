package com.lukasz.witkowski.android.moviestemple

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.lukasz.witkowski.android.moviestemple.models.Movie
import com.lukasz.witkowski.android.moviestemple.models.entities.MovieWithReviewsAndVideos
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow

class MainViewModel(application: Application) : ViewModel() {

    enum class Status {
        LOADING, SUCCESS, FAILURE
    }

    private val repository = MainRepository.getInstance(application)

    //val databaseValues: LiveData<List<Movie>>  = repository.databaseValues

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
            /*val movieToDelete = databaseValues.value?.find {
                it.id == _selectedMovie.value!!.id
            }
            repository.deleteMovieFromDatabase(movieToDelete!!)*/
            repository.deleteMovieFromDatabase(_selectedMovie.value!!)
        }
    }

   /* fun isSelectedMovieInDatabase(): Boolean{
        Log.i("MainViewModel", "database values ${databaseValues.value} selected item ${_selectedMovie.value}")
        databaseValues.value?.forEach {
            if(it.id == _selectedMovie.value!!.id){
                return true
            }
        }
        return false
    }*/


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

       /* if(isSelectedMovieInDatabase()){
            getDetailInformationFromDatabase()
        }else{
            requestApiForDetailInformation()
        }*/
    }

    var isMovieInDatabase = false


    fun isSelectedMovieInDatabase(){
        viewModelScope.launch {
            isMovieInDatabase = repository.isMovieInDatabase(_selectedMovie.value!!.id)
        }
        //return repository.isMovieInDatabase(_selectedMovie.value!!.id)
    }

   /* private fun getDetailInformationFromDatabase() {
        _selectedMovie.value = databaseValues.value?.find {
            it.id == _selectedMovie.value?.id
        }
        _requestDetailInformationStatus.value = Status.SUCCESS
    }*/


    /*private fun requestApiForDetailInformation(){
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
*/

    fun deleteAllFavouriteMovies(){
        viewModelScope.launch {
            repository.deleteAllFavouriteMovies()
        }
    }

    fun getRecommendationsBasedOnFavouriteMovies() {
        viewModelScope.launch {
             val recommends = repository.getRecommendationsBasedOnFavouriteMovies()
            Log.i("RecommendedMoviesModel", "recommends: $recommends")
        }
        Log.i("RecommendedMoviesModel", "favouriteMovies")
       // return repository.getRecommendationsBasedOnFavouriteMovies()
    }

}