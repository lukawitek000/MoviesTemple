package com.lukasz.witkowski.android.moviestemple

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.lukasz.witkowski.android.moviestemple.api.MoviesPagingSource
import com.lukasz.witkowski.android.moviestemple.database.FavouriteMovieDatabase
import com.lukasz.witkowski.android.moviestemple.models.*
import com.lukasz.witkowski.android.moviestemple.api.TMDBApi
import com.lukasz.witkowski.android.moviestemple.api.TMDB_PAGE_SIZE
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


    val favouriteMovies = Pager(
            config = PagingConfig(
                    pageSize = 20,
                    enablePlaceholders = false
            ),
            pagingSourceFactory = { database!!.movieDao().getAllMoviesPagingSource() }
    ).flow




    suspend fun getDetailInformation(movie: Movie): Movie{
        return withContext(IO) {
            if (isMovieInDatabase(movie.id)) {
                Log.i("Database debug", "movie details from database ")
                getMovieDetailsFromDatabase(movie.id)
            } else {
                Log.i("Database debug", "movie details from api ")
                getMovieDetailsFromApi(movie)
            }
        }
    }

    private suspend fun getMovieDetailsFromApi(movie: Movie): Movie {
        return withContext(IO) {
            val response = TMDBApi.retrofitService.getMovieDetailsVideosReviewsById(movieId = movie.id)
            movie.reviews  = response.reviews.results.map {
                it.toReview()
            }
            movie.videos = response.videos.results.map {
                it.toVideo()
            }
            movie.genres = response.genres
            movie
        }
    }

    private suspend fun getMovieDetailsFromDatabase(id: Int): Movie {
        return withContext(IO){
            val databaseEntity = database?.movieDao()?.getMovieWithVideosAndReviews(id)
            Log.i("Database debug", "movie details ${databaseEntity}")
            databaseEntity!!.toMovie()
        }
    }

    private suspend fun isMovieInDatabase(id: Int): Boolean {
        return withContext(IO) {
            database?.movieDao()?.isMovieInDatabase(id) ?: false
        }
    }


     fun isMovieInFavourites(id: Int): Boolean {
        return runBlocking {
            isMovieInDatabase(id)
        }
     }



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



    private suspend fun getFavouriteMoviesId(): List<Int> {
        return withContext(IO){
            val listOfIds: List<Int> =  database!!.movieDao().getAllMovies().map {
                it.movieId
            }
            listOfIds
        }
    }

     fun getRecommendationsBasedOnFavouriteMovies(): Flow<PagingData<Movie>>{
         return runBlocking {
             val listOfIds: List<Int> = getFavouriteMoviesId()
             Pager(
                     config = PagingConfig(
                             pageSize = TMDB_PAGE_SIZE,
                             enablePlaceholders = false,
                             initialLoadSize = 2 * TMDB_PAGE_SIZE,
                             prefetchDistance = TMDB_PAGE_SIZE
                     ),
                     pagingSourceFactory = { MoviesPagingSource(RECOMMENDATIONS_QUERY, listOfIds) }
             ).flow
         }
    }



    suspend fun deleteAllFavouriteMovies(){
        withContext(IO){
            database?.movieDao()?.deleteAll()
        }
    }

    fun getSearchedMovies(query: String): Flow<PagingData<Movie>> {
        return Pager(
                config = PagingConfig(
                        pageSize = 10,
                        enablePlaceholders = false
                ),
                pagingSourceFactory = { MoviesPagingSource(query)}
        ).flow

    }

}