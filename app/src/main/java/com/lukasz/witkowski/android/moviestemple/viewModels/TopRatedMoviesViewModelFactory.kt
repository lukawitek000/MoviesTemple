package com.lukasz.witkowski.android.moviestemple.viewModels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class TopRatedMoviesViewModelFactory(private val application: Application) : ViewModelProvider.Factory  {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TopRatedMoviesViewModel::class.java)) {
            return TopRatedMoviesViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}