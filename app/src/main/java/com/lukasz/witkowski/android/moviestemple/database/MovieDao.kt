package com.lukasz.witkowski.android.moviestemple.database

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.*
import com.lukasz.witkowski.android.moviestemple.models.Movie
import com.lukasz.witkowski.android.moviestemple.models.MovieWithReviewsAndVideos
import com.lukasz.witkowski.android.moviestemple.models.Review
import com.lukasz.witkowski.android.moviestemple.models.Video

@Dao
interface MovieDao {

    @Transaction
    @Query("SELECT * FROM Movie")
    fun loadAllMovies(): LiveData<List<MovieWithReviewsAndVideos>>


    @Query("SELECT * FROM Movie")
    fun getAllMovies(): LiveData<List<Movie>>

    @Transaction
    suspend fun insert(movie: Movie){
        insertMovie(movie)
        val videos = movie.videos
        videos.forEach {
            it.movieOwnerID = movie.id
            insertVideo(it)
        }
        val reviews = movie.reviews
        reviews.forEach {
            it.movieOwnerID = movie.id
            insertReview(it)
        }


    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: Review)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideo(video: Video)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: Movie)


    @Transaction
    suspend fun deleteMovieReviewAndVideo(movie: Movie){
        Log.i("Dao", "movie to delete = $movie")
        deleteMovieById(movie.id)
        deleteReviewByMovieOwnerId(movie.id)
        deleteVideoByMovieOwnerId(movie.id)
    }

    @Query("DELETE FROM Movie WHERE id = :id")
    suspend fun deleteMovieById(id: Long)

    @Query("DELETE FROM Review WHERE movieOwnerID = :id")
    suspend fun deleteReviewByMovieOwnerId(id: Long)

    @Query("DELETE FROM Video WHERE movieOwnerID = :id")
    suspend fun deleteVideoByMovieOwnerId(id: Long)


    @Transaction
    suspend fun deleteAll(){
        deleteAllReviews()
        deleteAllVideos()
        deleteAllMovies()
    }

    @Query("DELETE FROM Movie")
    suspend fun deleteAllMovies()

    @Query("DELETE FROM Review")
    suspend fun deleteAllReviews()

    @Query("DELETE FROM Video")
    suspend fun deleteAllVideos()
}