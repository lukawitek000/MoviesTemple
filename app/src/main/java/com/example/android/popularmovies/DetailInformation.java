package com.example.android.popularmovies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.android.popularmovies.database.DatabaseExecutor;
import com.example.android.popularmovies.database.FavouriteMovieDatabase;
import com.example.android.popularmovies.database.MovieEntity;
import com.example.android.popularmovies.databinding.ActivityDetailInfromationBinding;
import com.squareup.picasso.Picasso;

public class DetailInformation extends AppCompatActivity implements TrailersAdapter.TrailerClickListener {
    private Movie selectedMovie;
    private ActivityDetailInfromationBinding binding;
    private ReviewsAdapter reviewsAdapter;

    private TrailersAdapter trailersAdapter;

    private FavouriteMovieDatabase favouriteMovieDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView( this, R.layout.activity_detail_infromation);
        favouriteMovieDatabase = FavouriteMovieDatabase.getInstance(this);
        Intent intent = getIntent();
        setUpReviewsRecyclerView();
        setUpTrailersRecyclerView();


        int id = 0;
        if(intent != null){
            if(intent.hasExtra(MainActivity.MOVIE_KEY)){
                id = intent.getIntExtra(MainActivity.MOVIE_KEY, 0);
            }
            if(intent.hasExtra(MainActivity.TITLE_KEY)){
                setTitle(intent.getStringExtra(MainActivity.TITLE_KEY));
            }
        }
        findSelectedMovie(id);
        uploadData();
        setUpViewModel();

        binding.addToFavouriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseExecutor.getInstance().getDiskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        if(!selectedMovie.isFavourite()) {
                            selectedMovie.setFavourite(true);
                            favouriteMovieDatabase.movieDao().insertMovie(convertMovieToMovieEntity());
                        }else{
                            selectedMovie.setFavourite(false);
                            favouriteMovieDatabase.movieDao().deleteMovieById(selectedMovie.getId());
                        }
                    }
                });

            }

        });


        if(selectedMovie.getReviews().length == 0){
            binding.reviewsLabel.setVisibility(View.GONE);
            binding.recyclerviewReviews.setVisibility(View.GONE);
        }
        if(selectedMovie.getVideoUrls().length == 0){
            binding.trailersLabel.setVisibility(View.GONE);
            binding.recyclerviewTrailers.setVisibility(View.GONE);
        }



    }

    @SuppressWarnings("deprecation")
    private void setUpViewModel() {
        DetailInformationViewModelFactory factory = new DetailInformationViewModelFactory(favouriteMovieDatabase, selectedMovie.getId());
        DetailInformationViewModel viewModel = ViewModelProviders.of(this, factory).get(DetailInformationViewModel.class);

        viewModel.getMovie().observe(this, new Observer<MovieEntity>() {
            @Override
            public void onChanged(MovieEntity movieEntity) {
                if(movieEntity != null || selectedMovie.isFavourite()){
                    binding.addToFavouriteButton.setText(R.string.remove_from_favourites);
                    selectedMovie.setFavourite(true);
                }else {
                    binding.addToFavouriteButton.setText(R.string.add_to_favourites);
                    selectedMovie.setFavourite(false);
                }
            }
        });
    }

    private void setUpTrailersRecyclerView() {
        LinearLayoutManager trailerManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        trailersAdapter = new TrailersAdapter(this);
        binding.recyclerviewTrailers.setLayoutManager(trailerManager);
        binding.recyclerviewTrailers.setAdapter(trailersAdapter);
        binding.recyclerviewTrailers.setHasFixedSize(true);
    }

    private void setUpReviewsRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        reviewsAdapter = new ReviewsAdapter();
        binding.recyclerviewReviews.setLayoutManager(linearLayoutManager);
        binding.recyclerviewReviews.setAdapter(reviewsAdapter);
        binding.recyclerviewReviews.setHasFixedSize(true);
    }

    private MovieEntity convertMovieToMovieEntity() {
        MovieEntity movieEntity = new MovieEntity();
        movieEntity.setId(selectedMovie.getId());
        movieEntity.setOriginalTitle(selectedMovie.getOriginalTitle());
        movieEntity.setTitle(selectedMovie.getTitle());
        movieEntity.setPosterUri(selectedMovie.getPoster());
        movieEntity.setOverview(selectedMovie.getOverview());
        movieEntity.setVoteAverage(selectedMovie.getVoteAverage());
        movieEntity.setReleaseDate(selectedMovie.getReleaseDate());
        movieEntity.setVideoUrls(selectedMovie.getVideoUrls());
        movieEntity.setReviews(selectedMovie.getReviews());
        return movieEntity;

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
                binding.starsImageView.setImageDrawable(getDrawable(R.drawable.five_star));
            }else if (selectedMovie.getVoteAverage() > 6.0) {
                binding.starsImageView.setImageDrawable(getDrawable(R.drawable.four_star));
            }
            else if (selectedMovie.getVoteAverage() > 4.0) {
                binding.starsImageView.setImageDrawable(getDrawable(R.drawable.three_star));
            }
            else if (selectedMovie.getVoteAverage() > 2.0) {
                binding.starsImageView.setImageDrawable(getDrawable(R.drawable.two_star));
            }else{
                binding.starsImageView.setImageDrawable(getDrawable(R.drawable.one_star));
            }
        }else{
            binding.starsImageView.setVisibility(View.INVISIBLE);
        }
    }


    private void uploadData(){
        binding.title.setText(selectedMovie.getTitle());
        binding.originalTitle.setText(selectedMovie.getOriginalTitle());
        binding.overview.setText(selectedMovie.getOverview());
        binding.releaseDate.setText(selectedMovie.getReleaseDate());
        binding.voteAverage.setText(String.valueOf(selectedMovie.getVoteAverage()));
        trailersAdapter.setTrailers(selectedMovie.getVideoUrls());
        reviewsAdapter.setReviews(selectedMovie.getReviews());
        Picasso.with(this)
                .load(selectedMovie.getPoster())
                .into(binding.poster);
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


    @Override
    public void onTrailerClicked(String trailer) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + trailer));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + trailer));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }
}
