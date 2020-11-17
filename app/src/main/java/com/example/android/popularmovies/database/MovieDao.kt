package com.example.android.popularmovies.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.popularmovies.models.Movie
import com.example.android.popularmovies.models.MovieWithReviewsAndVideos
import com.example.android.popularmovies.models.Review
import com.example.android.popularmovies.models.Video

@Dao
interface MovieDao {

    @Transaction
    @Query("SELECT * FROM Movie")
    fun loadAllMovies(): LiveData<List<MovieWithReviewsAndVideos>>


    @Query("SELECT * FROM Movie")
    fun getAllMovies(): LiveData<List<Movie>>

    @Transaction
    suspend fun insert(movie: Movie){
        val id = insertMovie(movie)
        val videos = movie.trailers
        videos.forEach {
            it.movieOwnerID = id
            insertVideo(it)
        }
        val reviews = movie.reviews
        reviews.forEach {
            it.movieOwnerID = id
            insertReview(it)
        }


    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: Review)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideo(video: Video)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: Movie): Long
/*
    @Query("DELETE FROM Favorite_Movies WHERE id = :id")
    suspend fun deleteMovieById(id: Int)

    @Query(" SELECT * FROM Favorite_Movies WHERE id = :id ")
    fun loadMovieById(id: Int): LiveData<Movie>*/
}