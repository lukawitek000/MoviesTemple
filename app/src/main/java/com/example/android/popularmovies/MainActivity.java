package com.example.android.popularmovies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmovies.database.MovieEntity;
import com.example.android.popularmovies.utilities.MovieJsonConvert;
import com.example.android.popularmovies.utilities.NetworkUtil;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MovieAdapterOnClickHandler {

    private static final String SELECTED_SORTING = "SELECTED_SORTING";
    static final String MOVIE_KEY = "MOVIE";

    private static final String FAVOURITES = "favourites";
    private static final String TOP_RATED = "top_rated";
    private static final String POPULAR = "popular";
    private final static int IMAGE_WIDTH = 342;

    private MoviesAdapter movieAdapter;
    private String selectedSorting;
    private TextView failureTextView;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpRecyclerView();

        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.INVISIBLE);

        failureTextView = findViewById(R.id.failure_text_view);
        failureTextView.setVisibility(View.INVISIBLE);

        if(savedInstanceState != null){
            selectedSorting = savedInstanceState.getString(SELECTED_SORTING);
            assert selectedSorting != null;
            switch (selectedSorting){
                case FAVOURITES:
                    setTitle(R.string.favourites_title);
                    break;
                case POPULAR:
                    setTitle(R.string.popular_movie_title);
                    break;
                default:
                    setTitle(R.string.vote_average_label);
                    break;
            }
        }else{
            selectedSorting = POPULAR;
            setTitle(R.string.popular_movie_title);
        }

        if(selectedSorting.equals(FAVOURITES)) {
            setupViewModel();
        }else {
            updateUI();
        }


    }

    private void setUpRecyclerView() {
        int spanCount = calculateSpanCount();
        RecyclerView movieRecyclerView = findViewById(R.id.recyclerview_movies);
        GridLayoutManager layoutManager = new GridLayoutManager(this, spanCount, LinearLayoutManager.VERTICAL, false);
        movieRecyclerView.setLayoutManager(layoutManager);
        movieRecyclerView.setHasFixedSize(true);
        movieAdapter = new MoviesAdapter(this);
        movieRecyclerView.setAdapter(movieAdapter);
    }

    private int calculateSpanCount() {
        Point point = new Point();
        Display display = getWindowManager().getDefaultDisplay();
        display.getSize(point);
        int display_width = point.x;
        return Math.round((float) display_width/IMAGE_WIDTH);
    }

    private void setupViewModel() {
        @SuppressWarnings("deprecation") MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getFavouriteMovies().observe(this, new Observer<List<MovieEntity>>(){
            @Override
            public void onChanged(List<MovieEntity> movieEntities) {
                Movie[] movies = new Movie[movieEntities.size()];
                for(int i = 0; i< movieEntities.size(); i++){
                    movies[i] = getMovieFromMovieEntity(movieEntities.get(i));
                }
                movieAdapter.setMoviesData(movies);
            }

        });

    }

    private void updateUI() {
        new FetchMovies().execute();
    }


    @Override
    public void onClick(Movie movie) {
        Intent intent = new Intent(this, DetailInformation.class);
        intent.putExtra(MOVIE_KEY, movie.getId());
        startActivity(intent);
    }


    private Movie getMovieFromMovieEntity(MovieEntity movieEntity) {
        Movie movie = new Movie();
        movie.setId(movieEntity.getId());
        movie.setId(movieEntity.getId());
        movie.setOriginalTitle(movieEntity.getOriginalTitle());
        movie.setTitle(movieEntity.getTitle());
        movie.setPoster(movieEntity.getPosterUri());
        movie.setOverview(movieEntity.getOverview());
        movie.setVoteAverage(movieEntity.getVoteAverage());
        movie.setReleaseDate(movieEntity.getReleaseDate());
        movie.setVideoUrls(movieEntity.getVideoUrls());
        movie.setReviews(movieEntity.getReviews());
        movie.setFavourite(true);
        return movie;

    }


     @SuppressLint("StaticFieldLeak")
     class  FetchMovies extends AsyncTask<Void, Void, Movie[]>{

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            failureTextView.setVisibility(View.INVISIBLE);
        }

        @Override
        protected Movie[] doInBackground(Void... voids) {


            if(!Objects.equals(selectedSorting, FAVOURITES)) {
                URL movieUrl = NetworkUtil.buildUrl(selectedSorting);
                try {
                    String answer = NetworkUtil.getResponseFromHttpUrl(movieUrl);
                    Movie[] fetchedMovies = MovieJsonConvert.getMovieFromJson(answer);
                    getVideosFromApi(fetchedMovies);
                    getReviewsFromApi(fetchedMovies);
                    return fetchedMovies;
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }


         private void getReviewsFromApi(Movie[] fetchedMovies) throws IOException, JSONException {
             for(Movie movie : fetchedMovies){
                 URL movieUrl = NetworkUtil.buildUrl((movie.getId()) + "/reviews");
                 String answer = NetworkUtil.getResponseFromHttpUrl(movieUrl);
                 Review[] review = (MovieJsonConvert.getReviewsFromJson(answer));
                 movie.setReviews(review);
             }
         }

         private void getVideosFromApi(Movie[] fetchedMovies) throws IOException, JSONException {
            for(Movie movie : fetchedMovies){
                URL movieUrl = NetworkUtil.buildUrl((movie.getId()) + "/videos");
                String answer = NetworkUtil.getResponseFromHttpUrl(movieUrl);
                movie.setVideoUrls(MovieJsonConvert.getVideoUrlFromJson(answer));
            }
         }

         @Override
        protected void onPostExecute(Movie[] movies) {
            progressBar.setVisibility(View.INVISIBLE);
            if(movies != null){
                movieAdapter.setMoviesData(movies);
            }else {
                failureTextView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sort_by_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        switch (selectedSorting) {
            case POPULAR:
                menu.findItem(R.id.sort_by_popularity).setVisible(false);
                menu.findItem(R.id.sort_by_votes).setVisible(true);
                menu.findItem(R.id.show_favourites).setVisible(true);
                break;
            case TOP_RATED:
                menu.findItem(R.id.sort_by_votes).setVisible(false);
                menu.findItem(R.id.sort_by_popularity).setVisible(true);
                menu.findItem(R.id.show_favourites).setVisible(true);
                break;
            case FAVOURITES:
                menu.findItem(R.id.sort_by_votes).setVisible(true);
                menu.findItem(R.id.sort_by_popularity).setVisible(true);
                menu.findItem(R.id.show_favourites).setVisible(false);
                break;
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.sort_by_popularity){
            selectedSorting = POPULAR;
            setTitle(R.string.popular_movie_title);
            updateUI();
            return true;
        }else if(id == R.id.sort_by_votes){
            selectedSorting = TOP_RATED;
            setTitle(R.string.top_rated_title);
            updateUI();
            return true;
        }else if(id == R.id.show_favourites){
            selectedSorting = FAVOURITES;
            setupViewModel();
            setTitle(R.string.favourites_title);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(SELECTED_SORTING, selectedSorting);
        super.onSaveInstanceState(outState);
    }
}
