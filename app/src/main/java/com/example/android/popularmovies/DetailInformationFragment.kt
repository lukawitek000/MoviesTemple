package com.example.android.popularmovies

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.popularmovies.adapters.ReviewsAdapter
import com.example.android.popularmovies.adapters.TrailersAdapter
import com.example.android.popularmovies.adapters.TrailersAdapter.TrailerClickListener
import com.example.android.popularmovies.databinding.FragmentDetailInfromationBinding
import com.example.android.popularmovies.models.Video
import com.squareup.picasso.Picasso

class DetailInformationFragment : Fragment(), TrailerClickListener {

    private lateinit var trailersAdapter: TrailersAdapter

    private lateinit var reviewsAdapter: ReviewsAdapter

    private lateinit var viewModel: MainViewModel



    private lateinit var binding: FragmentDetailInfromationBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail_infromation, container, false)

        setUpViewModel();
        setUpReviewsRecyclerView();
        setUpTrailersRecyclerView();
        binding.title.text = viewModel.selectedMovie?.title

        binding.overview.text = viewModel.selectedMovie?.overview
        binding.originalTitle.text = viewModel.selectedMovie?.originalTitle
        binding.releaseDate.text = viewModel.selectedMovie?.releaseDate

        Picasso.with(context)
                .load(viewModel.selectedMovie?.posterUri)
                .into(binding.poster)
        //uploadData();

       setButtonText()

        viewModel.databaseValues.observe(viewLifecycleOwner, Observer {
            if(it != null){
                viewModel.setFavouriteMovies(it)
            }
        })


        binding.addToFavouriteButton.setOnClickListener {
            if(viewModel.isSelectedMovieInDatabase()){
                viewModel.deleteMovieFromDatabase()
                binding.addToFavouriteButton.text = resources.getString(R.string.add_to_favourites)
                Toast.makeText(requireContext(), "Removed from database", Toast.LENGTH_SHORT).show()
            }else {
                viewModel.addMovieToDatabase()
                binding.addToFavouriteButton.text = resources.getString(R.string.remove_from_favourites)
                Toast.makeText(requireContext(), "Added to database", Toast.LENGTH_SHORT).show()
            }
        }

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


        if(viewModel.selectedMovie!!.reviews.isEmpty()){
            binding.reviewsLabel.visibility = View.GONE
            binding.recyclerviewReviews.visibility = View.GONE
        }
        if(viewModel.selectedMovie!!.trailers.isEmpty()){
            binding.trailersLabel.visibility = View.GONE
            binding.recyclerviewTrailers.visibility = View.GONE
        }
        


        return binding.root
    }

    private fun setButtonText(){
        if(viewModel.isSelectedMovieInDatabase()){
            binding.addToFavouriteButton.text = resources.getString(R.string.remove_from_favourites)
        }else {
            binding.addToFavouriteButton.text = resources.getString(R.string.add_to_favourites)
        }
    }


        private fun setUpViewModel() {
            val viewModelFactory = MainViewModelFactory(requireActivity().application)
            viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(MainViewModel::class.java)
        }

        private fun setUpTrailersRecyclerView() {
            val trailerManager =  LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            trailersAdapter =  TrailersAdapter(viewModel.selectedMovie!!.trailers, this)
            binding.recyclerviewTrailers.layoutManager = trailerManager
            binding.recyclerviewTrailers.adapter = trailersAdapter
            binding.recyclerviewTrailers.setHasFixedSize(true)
        }

        private fun setUpReviewsRecyclerView() {
            val  linearLayoutManager =  LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            Log.i("DetailInformation", "set up recycler view reviews = ")
            reviewsAdapter = ReviewsAdapter(viewModel.selectedMovie!!.reviews)
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
    override fun onTrailerClicked(trailer: Video) {
        Toast.makeText(requireContext(), "clicked trailer ${trailer.name}", Toast.LENGTH_SHORT).show()
        val intent = Intent(Intent.ACTION_VIEW)
        if(trailer.site == "YouTube") {
            intent.data = trailer.videoUri
            startActivity(intent)
        }else{
            Toast.makeText(requireContext(), "Unknown site", Toast.LENGTH_SHORT).show()
        }

    }
}