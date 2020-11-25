package com.lukasz.witkowski.android.moviestemple.viewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.lukasz.witkowski.android.moviestemple.MainRepository
import com.lukasz.witkowski.android.moviestemple.MainViewModel
import com.lukasz.witkowski.android.moviestemple.models.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.lang.Exception

class RecommendedMoviesViewModel(application: Application): ViewModel() {

    private val repository = MainRepository.getInstance(application)

    val favouriteMovies = repository.favouriteMovies

    private val _recommendedMovies = MutableLiveData<Set<Movie>>()
    val recommendedMovies: LiveData<Set<Movie>>
        get() = _recommendedMovies


    private val _recommendedMoviesStatus = MutableLiveData<MainViewModel.Status>()
    val recommendedMoviesStatus: LiveData<MainViewModel.Status>
        get() = _recommendedMoviesStatus


    fun getRecommendationsBasedOnFavouriteMovies(): Flow<PagingData<Movie>> {
        Log.i("RecommendedMoviesModel", "favouriteMovies = ${favouriteMovies.value}")
        return repository.getRecommendationsBasedOnFavouriteMovies(favouriteMovies.value!!)
    }

    /*fun getRecommendationsBasedOnFavouriteMovies(){
        viewModelScope.launch {
            try {
                _recommendedMoviesStatus.value = MainViewModel.Status.LOADING
                val recommendationsList = mutableSetOf<Movie>()
                for(movie in favouriteMovies.value!!){
                    Log.i("RecommendedMoviesModel", "fetching recommendations ${movie.title}")
                    val response = repository.getRecommendationBasedOnMovieID(movie.id)
                    recommendationsList.addAll(response)
                }
               // _recommendedMovies.value = recommendationsList.shuffled().toSet()
                Log.i("RecommendedMoviesModel", "fetching recommendations")
                _recommendedMovies.value = recommendationsList.toSet()
                _recommendedMoviesStatus.value = MainViewModel.Status.SUCCESS
            }catch (e: Exception){
                Log.i("RecommendedMoviesModel", "Error with fetching recommendations")
                _recommendedMoviesStatus.value = MainViewModel.Status.FAILURE
            }
        }
    }*/
}