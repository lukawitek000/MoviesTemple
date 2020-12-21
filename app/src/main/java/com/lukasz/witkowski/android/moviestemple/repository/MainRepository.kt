package com.lukasz.witkowski.android.moviestemple.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.lukasz.witkowski.android.moviestemple.api.*
import com.lukasz.witkowski.android.moviestemple.database.MovieDao
import com.lukasz.witkowski.android.moviestemple.models.*
import com.lukasz.witkowski.android.moviestemple.api.responses.MovieDetailsResponse
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow

class MainRepository
constructor(
        private val movieDao: MovieDao,
        private val tmdbService: TMDBService
) {

    val favouriteMovies = Pager(
            config = PagingConfig(
                    pageSize = 20,
                    enablePlaceholders = false
            ),
            pagingSourceFactory = { movieDao.getAllMoviesPagingSource() }
    ).flow



    suspend fun getDetailInformation(movie: Movie): Movie{
        return withContext(IO) {
            if (isMovieInDatabase(movie.id)) {
                getMovieDetailsFromDatabase(movie.id)
            } else {
                getMovieDetailsFromApi(movie)
            }
        }
    }

    suspend fun getMovieDetailsFromApi(movie: Movie): Movie {
        return withContext(IO) {
            val response = tmdbService.getMovieDetailsVideosReviewsById(movieId = movie.id)
            movie.reviews  = response.reviews.results.map {
                it.toReview()
            }
            movie.videos = response.videos.results.map {
                it.toVideo()
            }
            movie.genres = response.genres
            movie.directors = getDirectors(response)
            movie.writers = getWriters(response)
            movie.cast = response.credits.cast.map {
                it.toActor()
            } as MutableList<Actor>
            movie
        }
    }

    private fun getDirectors(response: MovieDetailsResponse): List<Director>{
        val director = response.credits.crew.filter {
            it.job == "Director"
        }
        return director.map {
            it.toDirector()
        }
    }

    private fun getWriters(response: MovieDetailsResponse): List<Writer>{
        val writer = response.credits.crew.filter {
            it.job == "Writer"
        }
        return writer.map {
            it.toWriter()
        }
    }


    private suspend fun getMovieDetailsFromDatabase(id: Int): Movie {
        return withContext(IO){
            val databaseEntity = movieDao.getMovieWithVideosAndReviews(id)
            databaseEntity.toMovie()
        }
    }


    private suspend fun isMovieInDatabase(id: Int): Boolean {
        return withContext(IO) {
            movieDao.isMovieInDatabase(id)
        }
    }


    fun isMovieInFavourites(id: Int): Boolean {
        return runBlocking {
            isMovieInDatabase(id)
        }
    }


    suspend fun deleteMovieFromDatabase(movie: Movie){
        withContext(IO){
            movieDao.deleteMovieReviewAndVideo(movie.toMovieEntity())
        }
    }


    suspend fun insertMovieToDatabase(movie: Movie){
        withContext(IO){
            movieDao.insert(movie)
        }
    }


    private suspend fun getFavouriteMoviesId(): List<Int> {
        return withContext(IO){
            val listOfIds: List<Int> =  movieDao.getAllMovies().map {
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
                     pagingSourceFactory = { MoviesPagingSource(tmdbService, RECOMMENDATIONS_QUERY, listOfIds) }
             ).flow
         }
    }


    suspend fun deleteAllFavouriteMovies(){
        withContext(IO){
            movieDao.deleteAll()
        }
    }


    fun getPagingDataMovies(query: String): Flow<PagingData<Movie>> {
        return Pager(
                config = PagingConfig(
                        pageSize = TMDB_PAGE_SIZE,
                        enablePlaceholders = false,
                        initialLoadSize = 2 * TMDB_PAGE_SIZE,
                        prefetchDistance = TMDB_PAGE_SIZE
                ),
                pagingSourceFactory = { MoviesPagingSource(tmdbService, query) }
        ).flow
    }

}