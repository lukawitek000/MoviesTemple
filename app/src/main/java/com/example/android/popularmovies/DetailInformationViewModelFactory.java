package com.example.android.popularmovies;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.android.popularmovies.database.FavouriteMovieDatabase;

@SuppressWarnings("ALL")
public class DetailInformationViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final FavouriteMovieDatabase database;
    private final int movieId;

    public DetailInformationViewModelFactory(FavouriteMovieDatabase database, int movieId) {
        this.database = database;
        this.movieId = movieId;
    }




    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new DetailInformationViewModel(database, movieId);
    }
}
