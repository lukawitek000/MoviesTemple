package com.example.android.popularmovies.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM Favorite_Movies ORDER BY id")
    LiveData<List<MovieEntity>> loadAllMovies();

    @Insert
    void insertMovie(MovieEntity movieEntity);


    @Query("DELETE FROM Favorite_Movies WHERE id = :id")
    void deleteMovieById(int id);


    @Query(" SELECT * FROM Favorite_Movies WHERE id = :id ")
    LiveData<MovieEntity> loadMovieById(int id);



}
