package com.example.android.popularmovies;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.android.popularmovies.database.FavouriteMovieDatabase;
import com.example.android.popularmovies.database.MovieEntity;

@SuppressWarnings("WeakerAccess")
public class DetailInformationViewModel extends ViewModel{
    private final LiveData<MovieEntity> movie;

    public DetailInformationViewModel(FavouriteMovieDatabase database, int movieId) {
        movie = database.movieDao().loadMovieById(movieId);
    }


    public LiveData<MovieEntity> getMovie() {
        return movie;
    }
}
