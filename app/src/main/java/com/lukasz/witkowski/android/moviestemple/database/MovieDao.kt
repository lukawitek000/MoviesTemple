package com.lukasz.witkowski.android.moviestemple.database

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.*
import com.lukasz.witkowski.android.moviestemple.models.*
import com.lukasz.witkowski.android.moviestemple.models.entities.MovieWithReviewsAndVideos
import com.lukasz.witkowski.android.moviestemple.models.entities.MovieEntity
import com.lukasz.witkowski.android.moviestemple.models.entities.ReviewEntity
import com.lukasz.witkowski.android.moviestemple.models.entities.VideoEntity

@Dao
interface MovieDao {

    @Transaction
    @Query("SELECT * FROM Movies")
    fun loadAllMovies(): LiveData<List<MovieWithReviewsAndVideos>>


    @Query("SELECT * FROM Movies")
    fun getAllMovies(): LiveData<List<MovieEntity>>

    @Transaction
    suspend fun insert(movie: Movie){
        insertMovie(movie.toMovieEntity())
        val videos = movie.videos
        videos.forEach {
            val videoEntity = it.toVideoEntity()
            videoEntity.movieOwnerID = movie.id.toLong()
            insertVideo(videoEntity)
        }
        val reviews = movie.reviews
        reviews.forEach {
            val reviewEntity = it.toReviewEntity()
            reviewEntity.movieOwnerID = movie.id.toLong()
            insertReview(reviewEntity)
        }


    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: ReviewEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideo(video: VideoEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: MovieEntity)


    @Transaction
    suspend fun deleteMovieReviewAndVideo(movie: MovieEntity){
        Log.i("Dao", "movie to delete = $movie")
        deleteMovieById(movie.id)
        deleteReviewByMovieOwnerId(movie.id)
        deleteVideoByMovieOwnerId(movie.id)
    }

    @Query("DELETE FROM Movies WHERE id = :id")
    suspend fun deleteMovieById(id: Int)

    @Query("DELETE FROM Reviews WHERE movieOwnerID = :id")
    suspend fun deleteReviewByMovieOwnerId(id: Int)

    @Query("DELETE FROM Videos WHERE movieOwnerID = :id")
    suspend fun deleteVideoByMovieOwnerId(id: Int)


    @Transaction
    suspend fun deleteAll(){
        deleteAllReviews()
        deleteAllVideos()
        deleteAllMovies()
    }

    @Query("DELETE FROM Movies")
    suspend fun deleteAllMovies()

    @Query("DELETE FROM Reviews")
    suspend fun deleteAllReviews()

    @Query("DELETE FROM Videos")
    suspend fun deleteAllVideos()
}