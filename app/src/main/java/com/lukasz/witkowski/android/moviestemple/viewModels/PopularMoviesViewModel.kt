package com.lukasz.witkowski.android.moviestemple.viewModels

import android.app.Application
import android.app.DownloadManager
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

    fun getMovies(query: String): Flow<PagingData<Movie>>{
        return repository.getPagingDataMovies(query).cachedIn(viewModelScope)
    }

}