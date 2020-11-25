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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class TopRatedMoviesViewModel(application: Application): ViewModel() {


    private val repository = MainRepository.getInstance(application)


    private val _topRatedMovies = MutableLiveData<List<Movie>>()
    val topRatedMovies: LiveData<List<Movie>>
        get() = _topRatedMovies

    private val _topRatedMoviesStatus = MutableLiveData<MainViewModel.Status>()
    val topRatedMoviesStatus: LiveData<MainViewModel.Status>
        get() = _topRatedMoviesStatus

   /* fun getTopRatedMovies(){
        viewModelScope.launch {
            try {
                val startTime = System.currentTimeMillis()
                _topRatedMoviesStatus.value = MainViewModel.Status.LOADING
                _topRatedMovies.value = repository.getTopRatedMovies()
                _topRatedMoviesStatus.value = MainViewModel.Status.SUCCESS
                Log.i("MainViewModel", "time elapsed toprated for fetching data end = ${System.currentTimeMillis() - startTime}")
            } catch (e: Exception) {
                Log.i("MainViewModel", "failure e=$e")
                _topRatedMoviesStatus.value = MainViewModel.Status.FAILURE
            }
        }
    }*/


    fun getTopRatedMovies(): Flow<PagingData<Movie>> {
        return repository.getTopRatedMovies().cachedIn(viewModelScope) // cached in keep data when rotating
    }

}