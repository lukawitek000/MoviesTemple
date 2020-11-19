package com.example.android.popularmovies

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.android.popularmovies.models.Movie
import com.example.android.popularmovies.models.MovieWithReviewsAndVideos
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import java.lang.Exception

class MainViewModel(application: Application) : ViewModel() {

    /*
    enum class MovieTypeList{
        POPULAR_MOVIES, TOP_RATED_MOVIES, FAVOURITE_MOVIES
    }*/

    enum class Status {
        LOADING, SUCCESS, FAILURE
    }

    private val repository = MainRepository(application)

    //private lateinit var popularMovies: List<Movie>

    private val _popularMovies = MutableLiveData<List<Movie>>()

    val popularMovies: LiveData<List<Movie>>
        get() = _popularMovies

    //private lateinit var topRatedMovies: List<Movie>
    private val _topRatedMovies = MutableLiveData<List<Movie>>()

    val topRatedMovies: LiveData<List<Movie>>
        get() = _topRatedMovies
    private val _topRatedMoviesStatus = MutableLiveData<Status>()

    val topRatedMoviesStatus: LiveData<Status>
        get() = _topRatedMoviesStatus


    val databaseValues  = repository.favouriteMovies
    private var favouriteMovies: List<Movie> = emptyList()
    fun getFavouriteMovies(): List<Movie>{
        return favouriteMovies
    }


    //private var listType: MovieTypeList = MovieTypeList.POPULAR_MOVIES

    /*
    fun setListType(typeList: MovieTypeList){
        listType = typeList
        //setMoviesList()
    }*/

    //private val _movies = MutableLiveData<List<Movie>>()
    //val movies: LiveData<List<Movie>>
    //    get() = _movies


    private val _popularMoviesStatus = MutableLiveData<Status>()

    val popularMoviesStatus: LiveData<Status>
        get() = _popularMoviesStatus


    private val _recommendedMovies = MutableLiveData<Set<Movie>>()
    val recommendedMovies: LiveData<Set<Movie>>
    get() = _recommendedMovies

    private val _status = MutableLiveData<Status>()

    val status: LiveData<Status>
        get() = _status


    var selectedMovie: Movie? = null


    init {
        //setFavouriteMovies(databaseValues.value!!)
  //      getMovies()
        getPopularMovies()
        getTopRatedMovies()
    }

    fun getTopRatedMovies(){
        viewModelScope.launch {

            try {
                val startTime = System.currentTimeMillis()
                _topRatedMoviesStatus.value = Status.LOADING
                // getAllMovies()
               // Log.i("MainViewModel", "toprated = $popularMovies")
                // setMoviesList()
                val response = repository.getTopRatedMovies()
                Log.i("MainViewModel", "toprated = $response")
                _topRatedMovies.value = response
                _topRatedMoviesStatus.value = Status.SUCCESS
                Log.i("MainViewModel", "time elapsed for fetching data = ${System.currentTimeMillis() - startTime}")
            } catch (e: Exception) {
                Log.i("MainViewModel", "failure e=$e")
                _topRatedMoviesStatus.value = Status.FAILURE
            }
        }
    }

    fun getPopularMovies(){
        viewModelScope.launch {
            try {
                val startTime = System.currentTimeMillis()
                _popularMoviesStatus.value = Status.LOADING
               // getAllMovies()

               // setMoviesList()
                val response = repository.getPopularMovies()
                Log.i("MainViewModel", "popularmovies = $response")
                _popularMovies.value = response
                _popularMoviesStatus.value = Status.SUCCESS
                Log.i("MainViewModel", "time elapsed for fetching data = ${System.currentTimeMillis() - startTime}")
            } catch (e: Exception) {
                Log.i("MainViewModel", "failure e=$e")
                _popularMoviesStatus.value = Status.FAILURE
            }
        }
    }

/*
    private fun getMovies(){
        viewModelScope.launch {
            try {
                val startTime = System.currentTimeMillis()
                _popularMoviesStatus.value = Status.LOADING
                getAllMovies()
                Log.i("MainViewModel", "popularmovies = $popularMovies")
                setMoviesList()
                _popularMoviesStatus.value = Status.SUCCESS
                Log.i("MainViewModel", "time elapsed for fetching data = ${System.currentTimeMillis() - startTime}")
            } catch (e: Exception) {
                Log.i("MainViewModel", "failure e=$e")
                _popularMoviesStatus.value = Status.FAILURE
            }
        }
    }*/
/*
    private suspend fun getAllMovies() {
        withContext(IO) {
            val popularMovies: Deferred<List<Movie>> = async {
                repository.getPopularMovies()
            }
            val topRatedMovies: Deferred<List<Movie>> = async {
                repository.getTopRatedMovies()
            }
            this@MainViewModel.popularMovies = popularMovies.await()
            this@MainViewModel.topRatedMovies = topRatedMovies.await()
        }
    }
*/
    /*
    private fun setMoviesList(){
        if(listType == MovieTypeList.POPULAR_MOVIES && ::popularMovies.isInitialized){
            _movies.value = popularMovies
        }else if(listType == MovieTypeList.TOP_RATED_MOVIES  && ::topRatedMovies.isInitialized){
            _movies.value = topRatedMovies
        }else if (listType == MovieTypeList.FAVOURITE_MOVIES  && ::favouriteMovies.isInitialized){
            Log.i("MainViewModel", "favourite movies: $favouriteMovies")
            _movies.value = favouriteMovies
        }
    }*/


    fun addMovieToDatabase() {
        viewModelScope.launch {
            repository.insertMovieToDatabase(selectedMovie!!)
        }
    }

    fun setFavouriteMovies(response: List<MovieWithReviewsAndVideos>) {
        val movies = mutableListOf<Movie>()
        for(value in response){
            val movie = value.movie
            movie.videos = value.videos
            movie.reviews = value.reviews
            movies.add(movie)
        }
        favouriteMovies = movies
        getRecommendationsBasedOnFavouriteMovies()
        //setMoviesList()
    }


    fun deleteMovieFromDatabase(){
        viewModelScope.launch {
            val movieToDelete = favouriteMovies.find {
                it.id == selectedMovie!!.id
            }
            repository.deleteMovieFromDatabase(movieToDelete!!)
        }
    }

    fun isSelectedMovieInDatabase(): Boolean{
        favouriteMovies.forEach {
            if(it.id == selectedMovie!!.id){
                return true
            }
        }
        return false
    }

    private val _detailsStatus = MutableLiveData<Status>()
    val detailsStatus: LiveData<Status>
    get() = _detailsStatus

    fun getDetailInformation(){
        viewModelScope.launch {
            try{

                val start = System.currentTimeMillis()
                _detailsStatus.value = Status.LOADING
                repository.getMovieDetails(selectedMovie!!)
                _detailsStatus.value = Status.SUCCESS
                Log.i("MainViewModel", "time elaspsed ${System.currentTimeMillis() - start}")
            }catch (e: Exception){
                Log.i("MainViewModel", "errror $e")
                _detailsStatus.value = Status.FAILURE
            }
        }
    }


    fun getRecommendationsBasedOnFavouriteMovies(){
        viewModelScope.launch {
            if(_popularMoviesStatus.value == Status.SUCCESS) {
                val recommendationsList = mutableSetOf<Movie>()
                val recommendations = favouriteMovies.map {
                    async {
                        recommendationsList.addAll(repository.getRecommendationBasedOnMovieID(it.id))
                    }
                }
                recommendations.awaitAll()
                _recommendedMovies.value = recommendationsList
            }
        }
    }

}