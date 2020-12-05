package com.lukasz.witkowski.android.moviestemple.viewModels

import android.app.Application
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.lukasz.witkowski.android.moviestemple.MainRepository
import com.lukasz.witkowski.android.moviestemple.api.POPULAR_MOVIES_QUERY
import com.lukasz.witkowski.android.moviestemple.api.TOP_RATED_MOVIES_QUERY
import com.lukasz.witkowski.android.moviestemple.models.Movie
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow

class MainViewModel(application: Application) : ViewModel() {


    enum class ToolbarState{
        SEARCH, NORMAL
    }


    var toolbarState = ToolbarState.NORMAL
    /*private val _toolbarState = MutableLiveData<ToolbarState>(ToolbarState.NORMAL)
    val toolbarState: LiveData<ToolbarState>
        get() = _toolbarState


    fun setToolbarState(toolbarState: ToolbarState){
        _toolbarState.value = toolbarState
    }*/

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

    fun getMoreInfoForFavouriteMovie(){
        viewModelScope.launch {
            try {
                _requestDetailInformationStatus.value = Status.LOADING
                _selectedMovie.value = repository.getMovieDetailsFromApi(_selectedMovie.value!!)
                addMovieToDatabase()
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



    private var  popularMovies: Flow<PagingData<Movie>>? = null
    private var topRatedMovies: Flow<PagingData<Movie>>? = null

    var currentQueryValue: String? = null
    private var currentSearchResult: Flow<PagingData<Movie>>? = null

    fun getMovies(query: String): Flow<PagingData<Movie>>{
        if(query == POPULAR_MOVIES_QUERY){
            return fetchPopularMovies()
        }else if(query == TOP_RATED_MOVIES_QUERY){
            return fetchTopRatedMovies()
        }
        return fetchSearchedMovies(query)
    }

    private fun fetchSearchedMovies(query: String): Flow<PagingData<Movie>> {
        val lastResult = currentSearchResult
        if(query == currentQueryValue && lastResult != null){
            return lastResult
        }
        currentQueryValue = query
        val newResult = repository.getPagingDataMovies(query).cachedIn(viewModelScope)
        currentSearchResult = newResult
        return newResult
    }

    private fun fetchTopRatedMovies(): Flow<PagingData<Movie>> {
        if(topRatedMovies != null){
            return topRatedMovies!!
        }
        val newResult = repository.getPagingDataMovies(TOP_RATED_MOVIES_QUERY).cachedIn(viewModelScope)
        currentSearchResult = newResult
        topRatedMovies = newResult
        return newResult
    }

    private fun fetchPopularMovies(): Flow<PagingData<Movie>> {
        if(popularMovies != null){
            return popularMovies!!
        }
        val newResult = repository.getPagingDataMovies(POPULAR_MOVIES_QUERY).cachedIn(viewModelScope)
        currentSearchResult = newResult
        popularMovies = newResult
        return newResult
    }


    fun getRecommendationsBasedOnFavouriteMovies(): Flow<PagingData<Movie>> {
        return repository.getRecommendationsBasedOnFavouriteMovies()
    }

}