package com.lukasz.witkowski.android.moviestemple.viewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.lukasz.witkowski.android.moviestemple.MainRepository
import com.lukasz.witkowski.android.moviestemple.MainViewModel
import com.lukasz.witkowski.android.moviestemple.models.Movie
import com.lukasz.witkowski.android.moviestemple.models.responses.MovieGeneralInfoResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.lang.Exception

class PopularMoviesViewModel(application: Application): ViewModel() {

    private val repository = MainRepository.getInstance(application)

    private val _popularMovies = MutableLiveData<List<Movie>>()
    val popularMovies: LiveData<List<Movie>>
        get() = _popularMovies


    private val _popularMoviesStatus = MutableLiveData<MainViewModel.Status>()
    val popularMoviesStatus: LiveData<MainViewModel.Status>
        get() = _popularMoviesStatus

    /*fun getPopularMovies(){
        viewModelScope.launch {
            try {
                val startTime = System.currentTimeMillis()
                _popularMoviesStatus.value = MainViewModel.Status.LOADING
                _popularMovies.value = repository.getPopularMovies()
                _popularMoviesStatus.value = MainViewModel.Status.SUCCESS
                Log.i("PopularMoviesViewModel", "time elapsed popular for fetching data end = ${System.currentTimeMillis() - startTime}")
            } catch (e: Exception) {
                Log.i("PopularMoviesViewModel", "failure e=$e")
                _popularMoviesStatus.value = MainViewModel.Status.FAILURE
            }
        }
    }*/



    fun getPopularMovies(): Flow<PagingData<Movie>>{
        return repository.getPopularMovies().cachedIn(viewModelScope)
    }


}