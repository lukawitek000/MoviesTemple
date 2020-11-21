package com.example.android.popularmovies.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.popularmovies.MainActivity
import com.example.android.popularmovies.MainViewModel
import com.example.android.popularmovies.MainViewModelFactory
import com.example.android.popularmovies.R
import com.example.android.popularmovies.adapters.ReviewsAdapter
import com.example.android.popularmovies.adapters.VideosAdapter
import com.example.android.popularmovies.adapters.VideosAdapter.VideoClickListener
import com.example.android.popularmovies.databinding.FragmentDetailInfromationBinding
import com.example.android.popularmovies.models.Movie
import com.example.android.popularmovies.models.Video
import com.squareup.picasso.Picasso

class DetailInformationFragment : Fragment(), VideoClickListener {

    private lateinit var videosAdapter: VideosAdapter

    private lateinit var reviewsAdapter: ReviewsAdapter

    private lateinit var viewModel: MainViewModel

    private lateinit var binding: FragmentDetailInfromationBinding

    private lateinit var selectedMovie: Movie

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail_infromation, container, false)



        setUpViewModel()
        selectedMovie = viewModel.selectedMovie.value!!
        setVideosAndReviewsVisible(false)
        setUpObservers()
        setUpReviewsRecyclerView()
        setUpVideosRecyclerView()
        setDataToUI()
        handleButtonAction()

        return binding.root
    }



    private fun handleButtonAction() {
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
    }


    private fun setUpObservers() {
        viewModel.requestDetailInformationStatus.observe(viewLifecycleOwner, Observer {
            if(it != null){
                when(it){
                    MainViewModel.Status.SUCCESS -> {
                        //   binding.addToFavouriteButton.isEnabled = true
                        setVideosAndReviewsVisible(true)
                    }
                    MainViewModel.Status.FAILURE -> {
                        //binding.addToFavouriteButton.isEnabled = false
                        setVideosAndReviewsVisible(false)
                        binding.detailInformationProgressbar.visibility = View.GONE
                    }
                    else -> {
                        //  binding.addToFavouriteButton.isEnabled = false
                        binding.detailInformationProgressbar.visibility = View.VISIBLE
                        setVideosAndReviewsVisible(false)
                    }
                }
            }
        })
        viewModel.databaseValues.observe(viewLifecycleOwner, Observer {
            if(it != null){
                viewModel.setResponseFromDatabaseToFavouriteMovies(it)
            }
        })

        viewModel.selectedMovie.observe(viewLifecycleOwner, Observer {
            if(it != null){
                videosAdapter.videos = it.videos
                reviewsAdapter.reviews = it.reviews
                selectedMovie = it
            }
        })
    }

    private fun setVideosAndReviewsVisible(isVisible: Boolean) {
        if(isVisible){
            binding.detailInformationProgressbar.visibility = View.GONE
            setVideosAndReviewsVisibility(View.VISIBLE)
            hideRecyclerViewsIfTheyAreEmpty()
        }else{
            setVideosAndReviewsVisibility(View.GONE)
        }
    }

    private fun setVideosAndReviewsVisibility(visibility: Int){
        binding.videosLabel.visibility = visibility
        binding.recyclerviewVideos.visibility = visibility
        binding.reviewsLabel.visibility = visibility
        binding.recyclerviewReviews.visibility = visibility
    }

    private fun hideRecyclerViewsIfTheyAreEmpty(){
        if(selectedMovie.reviews.isEmpty()){
            binding.reviewsLabel.visibility = View.GONE
            binding.recyclerviewReviews.visibility = View.GONE
        }
        if(selectedMovie.videos.isEmpty()){
            binding.videosLabel.visibility = View.GONE
            binding.recyclerviewVideos.visibility = View.GONE
        }
    }


    private fun setDataToUI(){
        binding.title.text = selectedMovie.title
        binding.overview.text = selectedMovie.overview
        binding.originalTitle.text = selectedMovie.originalTitle
        binding.releaseDate.text = selectedMovie.releaseDate
        binding.voteAverageTextview.text = selectedMovie.voteAverage.toString()
        Picasso.with(context)
                .load(selectedMovie.posterUri)
                .into(binding.poster)

        setButtonText()
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

    private fun setUpVideosRecyclerView() {
        val videoManager =  LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        videosAdapter =  VideosAdapter(this)
        binding.recyclerviewVideos.layoutManager = videoManager
        binding.recyclerviewVideos.adapter = videosAdapter
        binding.recyclerviewVideos.setHasFixedSize(true)
    }

    private fun setUpReviewsRecyclerView() {
        val  linearLayoutManager =  LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        Log.i("DetailInformation", "set up recycler view reviews = ")
        reviewsAdapter = ReviewsAdapter()
        binding.recyclerviewReviews.layoutManager = linearLayoutManager
        binding.recyclerviewReviews.adapter = reviewsAdapter
        binding.recyclerviewReviews.setHasFixedSize(true)
    }



    override fun onVideoClicked(video: Video) {
        Toast.makeText(requireContext(), "clicked video ${video.name}", Toast.LENGTH_SHORT).show()
        val intent = Intent(Intent.ACTION_VIEW)
        if(video.site == "YouTube") {
            intent.data = video.videoUri
            startActivity(intent)
        }else{
            Toast.makeText(requireContext(), "Unknown site", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onStop() {
        (requireActivity() as MainActivity).setBottomNavigationVisibility(View.VISIBLE)
        super.onStop()
    }

    override fun onStart() {
        (requireActivity() as MainActivity).setBottomNavigationVisibility(View.GONE)
        super.onStart()
    }
}