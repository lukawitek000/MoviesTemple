package com.example.android.popularmovies.database

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.popularmovies.models.Movie
import com.example.android.popularmovies.models.MovieWithReviewsAndVideos
import com.example.android.popularmovies.models.Review
import com.example.android.popularmovies.models.Video
import retrofit2.http.DELETE

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


    @Transaction
    suspend fun deleteMovieReviewAndVideo(movie: Movie){
        Log.i("Dao", "movie to delete = $movie")
        val id = deleteMovieById(movie.movieID)
        deleteReviewByMovieOwnerId(movie.movieID)
        deleteVideoByMovieOwnerId(movie.movieID)
    }

    @Query("DELETE FROM Movie WHERE movieID = :id")
    suspend fun deleteMovieById(id: Long)

    @Query("DELETE FROM Review WHERE movieOwnerID = :id")
    suspend fun deleteReviewByMovieOwnerId(id: Long)

    @Query("DELETE FROM Video WHERE movieOwnerID = :id")
    suspend fun deleteVideoByMovieOwnerId(id: Long)
/*
    @Query("DELETE FROM Favorite_Movies WHERE id = :id")
    suspend fun deleteMovieById(id: Int)

    @Query(" SELECT * FROM Favorite_Movies WHERE id = :id ")
    fun loadMovieById(id: Int): LiveData<Movie>*/
}