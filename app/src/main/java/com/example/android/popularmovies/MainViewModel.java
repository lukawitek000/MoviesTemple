package com.example.android.popularmovies;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.android.popularmovies.database.FavouriteMovieDatabase;
import com.example.android.popularmovies.database.MovieEntity;
import com.example.android.popularmovies.utilities.MovieJsonConvert;
import com.example.android.popularmovies.utilities.NetworkUtil;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private LiveData<List<MovieEntity>> favouriteMovies;



    public MainViewModel(@NonNull Application application) {
        super(application);
        FavouriteMovieDatabase database = FavouriteMovieDatabase.getInstance(this.getApplication());
        favouriteMovies = database.movieDao().loadAllMovies();
    }


    public LiveData<List<MovieEntity>> getFavouriteMovies(){
        return favouriteMovies;
    }

}
