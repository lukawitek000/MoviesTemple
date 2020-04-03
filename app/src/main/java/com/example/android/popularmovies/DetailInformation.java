package com.example.android.popularmovies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailInformation extends AppCompatActivity {


    private TextView title;
    private ImageView poster;
    private TextView originalTitle;
    private TextView overview;
    private TextView voteAverage;
    private TextView releaseDate;

    private Movie selectedMovie;
    private ImageView starRate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_infromation);
        setTitle(R.string.movie_detail_title);
        title = findViewById(R.id.title);
        originalTitle = findViewById(R.id.original_title);
        overview = findViewById(R.id.overview);
        poster = findViewById(R.id.poster);
        voteAverage = findViewById(R.id.vote_average);
        releaseDate = findViewById(R.id.release_date);
        starRate = findViewById(R.id.stars_image_view);

        Intent intent = getIntent();

        int id = 0;
        if(intent != null){
            if(intent.hasExtra("MOVIE")){
                id = intent.getIntExtra("MOVIE", 0);
            }
        }
        findSelectedMovie(id);




        uploadData();
    }

    private void findSelectedMovie(int id) {
        Movie[] movies = MoviesAdapter.getMoviesData();
        for(Movie movie : movies){
            if(movie.getId() == id){
                selectedMovie = movie;
                break;
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (selectedMovie.getVoteAverage() > 8.0) {
                starRate.setImageDrawable(getDrawable(R.drawable.five_star));
            }else if (selectedMovie.getVoteAverage() > 6.0) {
                starRate.setImageDrawable(getDrawable(R.drawable.four_star));
            }
            else if (selectedMovie.getVoteAverage() > 4.0) {
                starRate.setImageDrawable(getDrawable(R.drawable.three_star));
            }
            else if (selectedMovie.getVoteAverage() > 2.0) {
                starRate.setImageDrawable(getDrawable(R.drawable.two_star));
            }else{
                starRate.setImageDrawable(getDrawable(R.drawable.one_star));
            }
        }else{
            starRate.setVisibility(View.INVISIBLE);
        }
    }


    private void uploadData(){
        title.setText(selectedMovie.getTitle());
        originalTitle.setText(selectedMovie.getOriginalTitle());
        overview.setText(selectedMovie.getOverview());
        releaseDate.setText(selectedMovie.getReleaseDate());
        voteAverage.setText(String.valueOf(selectedMovie.getVoteAverage()));
        Picasso.with(this)
                .load(selectedMovie.getPoster())
                .into(poster);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.share_item);
        menuItem.setIntent(createShareMovieIntent());
        return true;
    }

    private Intent createShareMovieIntent() {
        return ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText(selectedMovie.getTitle() + " " + selectedMovie.getOverview())
                .getIntent();
    }


}
