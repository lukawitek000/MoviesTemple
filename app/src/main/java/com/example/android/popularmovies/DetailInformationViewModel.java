package com.example.android.popularmovies;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.android.popularmovies.database.FavouriteMovieDatabase;
import com.example.android.popularmovies.database.MovieEntity;

public class DetailInformationViewModel extends ViewModel{
    private LiveData<MovieEntity> movie;

    // COMPLETED (8) Create a constructor where you call loadTaskById of the taskDao to initialize the tasks variable
    // Note: The constructor should receive the database and the taskId
    public DetailInformationViewModel(FavouriteMovieDatabase database, int movieId) {
        movie = database.movieDao().loadMovieById(movieId);
        Log.i("ViewModel", "LiveData = " + movie.toString());
        Log.i("ViewModel", "Value = " + movie.getValue());
    }

    // COMPLETED (7) Create a getter for the task variable
    public LiveData<MovieEntity> getMovie() {
        return movie;
    }
}
