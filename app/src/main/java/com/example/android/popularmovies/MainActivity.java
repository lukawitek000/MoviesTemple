package com.example.android.popularmovies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

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

import com.example.android.popularmovies.database.FavouriteMovieDatabase;
import com.example.android.popularmovies.database.MovieEntity;
import com.example.android.popularmovies.utilities.MovieJsonConvert;
import com.example.android.popularmovies.utilities.NetworkUtil;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MovieAdapterOnClickHandler {
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        updateUI();
    }

    private static final String FAVOURITES = "favourites";
    private TextView failureTextView;

    private MoviesAdapter movieAdapter;
    private static final String TOP_RATED = "top_rated";
    private static final String POPULAR = "popular";
    private String selectedSorting;

    private ProgressBar progressBar;
    private final static int IMAGE_WIDTH = 342;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Point point = new Point();
        Display display = getWindowManager().getDefaultDisplay();
        display.getSize(point);
        int display_width = point.x;
        int spanCount = Math.round((float) display_width/IMAGE_WIDTH);


        selectedSorting = POPULAR;
        setTitle(R.string.popular_movie_title);
        RecyclerView movieRecyclerView = findViewById(R.id.recyclerview_movies);
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.INVISIBLE);

        GridLayoutManager layoutManager = new GridLayoutManager(this, spanCount, LinearLayoutManager.VERTICAL, false);

        movieRecyclerView.setLayoutManager(layoutManager);
        movieRecyclerView.setHasFixedSize(true);

        movieAdapter = new MoviesAdapter(this);
        movieRecyclerView.setAdapter(movieAdapter);

        failureTextView = findViewById(R.id.failure_text_view);
        failureTextView.setVisibility(View.INVISIBLE);
        updateUI();




    }

    private void updateUI() {
        new FetchMovies().execute();
    }



    @Override
    public void onClick(Movie movie) {
        Intent intent = new Intent(this, DetailInformation.class);
        intent.putExtra("MOVIE", movie.getId());
        startActivity(intent);
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


            if(selectedSorting != FAVOURITES) {
                URL movieUrl = NetworkUtil.buildUrl(selectedSorting);
                try {
                    String answer = NetworkUtil.getResponseFromHttpUrl(movieUrl);
                    Movie[] fetchedMovies = MovieJsonConvert.getMovieFromJson(answer, MainActivity.this);
                    getVideosFromApi(fetchedMovies);
                    getReviewsFromApi(fetchedMovies);
                    return fetchedMovies;
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }else{
                List<MovieEntity> movieEntities = FavouriteMovieDatabase.getInstance(MainActivity.this).movieDao().loadAllMovies();
                Movie[] movies = new Movie[movieEntities.size()];
                for(int i = 0; i< movieEntities.size(); i++){
                    movies[i] = getMovieFromMovieEntity(movieEntities.get(i));
                }
                return movies;
            }
            return null;
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
        if(selectedSorting.equals(POPULAR)){
            //menu.findItem(R.id.sort_by).setTitle(R.string.sort_by_top_rated);
            menu.findItem(R.id.sort_by_popularity).setVisible(false);
            menu.findItem(R.id.sort_by_votes).setVisible(true);
            menu.findItem(R.id.show_favourites).setVisible(true);
        }else if(selectedSorting.equals(TOP_RATED)){
            //menu.findItem(R.id.sort_by).setTitle(R.string.sort_by_popularity);
            menu.findItem(R.id.sort_by_votes).setVisible(false);
            menu.findItem(R.id.sort_by_popularity).setVisible(true);
            menu.findItem(R.id.show_favourites).setVisible(true);
        }else if(selectedSorting.equals(FAVOURITES)){
            menu.findItem(R.id.sort_by_votes).setVisible(true);
            menu.findItem(R.id.sort_by_popularity).setVisible(true);
            menu.findItem(R.id.show_favourites).setVisible(false);
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
            setTitle(R.string.favourites_title);
            updateUI();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
