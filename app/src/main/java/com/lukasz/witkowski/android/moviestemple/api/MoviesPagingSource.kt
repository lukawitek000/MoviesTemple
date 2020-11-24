package com.lukasz.witkowski.android.moviestemple.api

import androidx.paging.PagingSource
import com.lukasz.witkowski.android.moviestemple.models.Movie
import com.lukasz.witkowski.android.moviestemple.models.responses.MovieGeneralInfoResponse
import com.lukasz.witkowski.android.moviestemple.models.toMovie
import java.io.IOException
import java.lang.Exception

class MoviesPagingSource(private val query: String) : PagingSource<Int, Movie>()  {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val position = params.key ?: TMDB_STARTING_PAGE_INDEX
        return try {
            val response = TMDBApi.retrofitService.getPopularMovies(page = position)
            val movies = response.movies.map {
                it.toMovie()
            }
            LoadResult.Page(
                    data = movies,
                    prevKey = if(position == TMDB_STARTING_PAGE_INDEX) null else position - 1,
                    nextKey = if(movies.isEmpty()) null else position + 1
            )
        }catch (e: Exception){
            LoadResult.Error(e)
        }
    }
}