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
import com.lukasz.witkowski.android.moviestemple.api.TOP_RATED_MOVIES_QUERY
import com.lukasz.witkowski.android.moviestemple.models.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class TopRatedMoviesViewModel(application: Application): ViewModel() {


    private val repository = MainRepository.getInstance(application)


    fun getTopRatedMovies(): Flow<PagingData<Movie>> {
        return repository.getPagingDataMovies(TOP_RATED_MOVIES_QUERY).cachedIn(viewModelScope) // cached in keep data when rotating
    }

}