package com.lukasz.witkowski.android.moviestemple

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.lukasz.witkowski.android.moviestemple.api.MoviesPagingSource
import com.lukasz.witkowski.android.moviestemple.database.FavouriteMovieDatabase
import com.lukasz.witkowski.android.moviestemple.models.*
import com.lukasz.witkowski.android.moviestemple.api.TMDBApi
import com.lukasz.witkowski.android.moviestemple.api.TMDB_PAGE_SIZE
import com.lukasz.witkowski.android.moviestemple.models.responses.MovieGeneralInfoResponse
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow

class MainRepository(application: Application) {


    companion object {
        private val LOCK = Any()
        @Volatile
        private var instance: MainRepository? = null
        fun getInstance(application: Application): MainRepository {
            return instance ?: synchronized(LOCK){
                MainRepository(application)
            }
        }

        const val TOP_RATED_MOVIES_QUERY = "top rated movies"
        const val POPULAR_MOVIES_QUERY = "popular movies"
        const val RECOMMENDATIONS_QUERY = "recommendations"
    }



    private val database = FavouriteMovieDatabase.getInstance(application.applicationContext)

    private val databaseResponse = database!!.movieDao().getAllData()

    val databaseValues: LiveData<List<Movie>> = Transformations.map(databaseResponse){ responseList ->
        responseList.map {
            it.toMovie()
        }
    }


    val favouriteMovies = Pager(
            config = PagingConfig(
                    pageSize = 20,
                    enablePlaceholders = false
            ),
            pagingSourceFactory = { database!!.movieDao().getAllMoviesPagingSource() }
    ).flow





    suspend fun deleteMovieFromDatabase(movie: Movie){
        withContext(IO){
            database!!.movieDao().deleteMovieReviewAndVideo(movie.toMovieEntity())
        }
    }


    suspend fun insertMovieToDatabase(movie: Movie){
        withContext(IO){
            database?.movieDao()?.insert(movie)
        }
    }


    fun getPopularMovies(): Flow<PagingData<Movie>> {
        return Pager(
                config = PagingConfig(
                        pageSize = TMDB_PAGE_SIZE,
                        enablePlaceholders = false,
                        initialLoadSize = 2 * TMDB_PAGE_SIZE,
                        prefetchDistance = TMDB_PAGE_SIZE
                ),
                pagingSourceFactory = { MoviesPagingSource(POPULAR_MOVIES_QUERY) }
        ).flow
    }


    fun getTopRatedMovies(): Flow<PagingData<Movie>> {
        return Pager(
                config = PagingConfig(
                        pageSize = TMDB_PAGE_SIZE,
                        enablePlaceholders = false,
                        initialLoadSize = 2 * TMDB_PAGE_SIZE,
                        prefetchDistance = TMDB_PAGE_SIZE
                ),
                pagingSourceFactory = { MoviesPagingSource(TOP_RATED_MOVIES_QUERY) }
        ).flow
    }



    fun getRecommendationsBasedOnFavouriteMovies(favouriteMovies: List<Movie>): Flow<PagingData<Movie>>{
        val listOfIds = favouriteMovies.map {
            it.id
        }
        Log.i("RecommendedMoviesModel", "main repo listof ids $listOfIds")
        return Pager(
                config = PagingConfig(
                        pageSize = TMDB_PAGE_SIZE,
                        enablePlaceholders = false,
                        initialLoadSize = 2 * TMDB_PAGE_SIZE,
                        prefetchDistance = TMDB_PAGE_SIZE
                ),
                pagingSourceFactory = { MoviesPagingSource(RECOMMENDATIONS_QUERY, listOfIds) }
        ).flow


    }



    suspend fun getMovieDetails(movie: Movie): Movie {
        return (IO) {
           // delay(10000)
            val response = TMDBApi.retrofitService.getMovieDetailsVideosReviewsById(movie.id)
            movie.reviews  = response.reviews.results.map {
                it.toReview()
            }
            movie.videos = response.videos.results.map {
                it.toVideo()
            }
            movie
        }
    }





    /*suspend fun getRecommendationBasedOnMovieID(movieID: Int): List<Movie> {
        return withContext(IO){
            val response = TMDBApi.retrofitService.getRecommendationsBaseOnMovieID(movieID)
            response.movies.map {
                it.toMovie()
            }
        }
    }*/


    suspend fun deleteAllFavouriteMovies(){
        withContext(IO){
            database?.movieDao()?.deleteAll()
        }
    }

}