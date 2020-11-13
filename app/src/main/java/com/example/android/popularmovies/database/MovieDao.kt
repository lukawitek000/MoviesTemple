package com.example.android.popularmovies.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MovieDao {
    @Query("SELECT * FROM Favorite_Movies ORDER BY id")
    fun loadAllMovies(): LiveData<List<MovieEntity?>?>?

    @Insert
    fun insertMovie(movieEntity: MovieEntity?)

    @Query("DELETE FROM Favorite_Movies WHERE id = :id")
    fun deleteMovieById(id: Int)

    @Query(" SELECT * FROM Favorite_Movies WHERE id = :id ")
    fun loadMovieById(id: Int): LiveData<MovieEntity?>?
}