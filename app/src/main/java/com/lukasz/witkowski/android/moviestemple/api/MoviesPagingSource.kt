package com.lukasz.witkowski.android.moviestemple.api

import android.util.Log
import androidx.paging.PagingSource
import com.lukasz.witkowski.android.moviestemple.MainRepository.Companion.POPULAR_MOVIES_QUERY
import com.lukasz.witkowski.android.moviestemple.MainRepository.Companion.RECOMMENDATIONS_QUERY
import com.lukasz.witkowski.android.moviestemple.MainRepository.Companion.TOP_RATED_MOVIES_QUERY
import com.lukasz.witkowski.android.moviestemple.models.Movie
import com.lukasz.witkowski.android.moviestemple.models.responses.TMDBResponse
import com.lukasz.witkowski.android.moviestemple.models.toMovie
import java.lang.Exception

class MoviesPagingSource(private val query: String, private val favouriteMoviesIds: List<Int> = emptyList()) : PagingSource<Int, Movie>()  {


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val position = params.key ?: TMDB_STARTING_PAGE_INDEX
        return try {
            val movies = getResponse(position)
            LoadResult.Page(
                    data = movies,
                    prevKey = if(position == TMDB_STARTING_PAGE_INDEX) null else position - 1,
                    nextKey = if(movies.isEmpty()) null else position + 1
            )
        }catch (e: Exception){
            LoadResult.Error(e)
        }
    }

    private suspend fun getResponse(position: Int): List<Movie> {
        if(query != RECOMMENDATIONS_QUERY){
            val response: TMDBResponse = when(query){
                POPULAR_MOVIES_QUERY -> TMDBApi.retrofitService.getPopularMovies(page = position)
                TOP_RATED_MOVIES_QUERY -> TMDBApi.retrofitService.getTopRatedMovies(page = position)
                else -> {
                    TMDBApi.retrofitService.getSearchMovies(query = query, page = position)
                }
            }

             return response.movies.map {
                it.toMovie()
            }
        }
        return getRecommendationsResponse(position)

    }


    private suspend fun getRecommendationsResponse(position: Int): List<Movie>{
        val listOfMovies = mutableListOf<Movie>()
        if(favouriteMoviesIds.isNotEmpty()){
            favouriteMoviesIds.forEach { movieId ->
                val response = TMDBApi.retrofitService.getRecommendationsBaseOnMovieID(movieId = movieId, page = position)
                listOfMovies.addAll(response.movies.map { it.toMovie() })
            }
        }
        Log.i("RecommendedMoviesModel", "paging source $listOfMovies")
        return listOfMovies

    }
}