package com.lukasz.witkowski.android.moviestemple.viewModels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lukasz.witkowski.android.moviestemple.MainViewModel

class PopularMoviesViewModelFactory(val application: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PopularMoviesViewModel::class.java)) {
            return PopularMoviesViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}