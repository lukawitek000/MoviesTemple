package com.example.android.popularmovies

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.popularmovies.adapters.ReviewsAdapter
import com.example.android.popularmovies.adapters.TrailersAdapter
import com.example.android.popularmovies.adapters.TrailersAdapter.TrailerClickListener
import com.example.android.popularmovies.databinding.FragmentDetailInfromationBinding

class DetailInformationFragment : Fragment(), TrailerClickListener {

    private lateinit var trailersAdapter: TrailersAdapter

    private lateinit var reviewsAdapter: ReviewsAdapter

    //private lateinit var viewModel: DetailInformationViewModel



    private lateinit var binding: FragmentDetailInfromationBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail_infromation, container, false)

        //setUpViewModel();
        //setUpReviewsRecyclerView();
        //setUpTrailersRecyclerView();





        //uploadData();

/*
        binding.addToFavouriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseExecutor.getInstance().getDiskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        if(!selectedMovie.isFavourite()) {
                            selectedMovie.setFavourite(true);
                            //favouriteMovieDatabase.movieDao().insertMovie(convertMovieToMovieEntity());
                        }else{
                            selectedMovie.setFavourite(false);
                            favouriteMovieDatabase.movieDao().deleteMovieById(selectedMovie.getId());
                        }
                    }
                });

            }

        });*/

/*
        if(selectedMovie.getReviews().length == 0){
            binding.reviewsLabel.setVisibility(View.GONE);
            binding.recyclerviewReviews.setVisibility(View.GONE);
        }
        if(selectedMovie.getVideoUrls().length == 0){
            binding.trailersLabel.setVisibility(View.GONE);
            binding.recyclerviewTrailers.setVisibility(View.GONE);
        }


*/


        return binding.root
    }


        private fun setUpViewModel() {
           // viewModel = ViewModelProvider(this).get(DetailInformationViewModel::class.java)
        }

        private fun setUpTrailersRecyclerView() {
            val trailerManager =  LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            trailersAdapter =  TrailersAdapter(null, this)
            binding.recyclerviewTrailers.layoutManager = trailerManager
            binding.recyclerviewTrailers.adapter = trailersAdapter
            binding.recyclerviewTrailers.setHasFixedSize(true)
        }

        private fun setUpReviewsRecyclerView() {
            val  linearLayoutManager =  LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            Log.i("DetailInformation", "set up recycler view reviews = ")
            reviewsAdapter = ReviewsAdapter(null)
            binding.recyclerviewReviews.layoutManager = linearLayoutManager
            binding.recyclerviewReviews.adapter = reviewsAdapter
            binding.recyclerviewReviews.setHasFixedSize(true)
        }



/*
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

*/
    override fun onTrailerClicked(trailer: String?) {
        /* Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + trailer));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + trailer));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }*/
    }
}