package com.example.android.popularmovies.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
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

        Log.i("DetailInformation", "on create view")
        setUpViewModel()
        selectedMovie = viewModel.selectedMovie.value!!
        setVideosAndReviewsVisible(false)
        setUpObservers()
        setUpReviewsRecyclerView()
        setUpVideosRecyclerView()
        setDataToUI()
        setHasOptionsMenu(true)
        Log.i("DetailInformation", "on create view end")
        return binding.root
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
        (requireActivity() as MainActivity).setBottomNavigationVisibility(View.VISIBLE, true)
        super.onStop()
    }

    private var showEnterAnimation = true

    override fun onStart() {
        super.onStart()
        (requireActivity() as MainActivity).setBottomNavigationVisibility(View.GONE, showEnterAnimation)
        showEnterAnimation = false
    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.detail_menu, menu)
        val item = menu.findItem(R.id.like_item)
        Log.i("DetailInformationFra", "on create menu ${item?.itemId}")
        if(item != null) {
            if (viewModel.isSelectedMovieInDatabase()) {
                item.icon = ResourcesCompat.getDrawable(resources, R.drawable.id_favorite, null)
            } else {
                item.icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_empty_favourite_icon, null)
            }
        }

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId){
       /* R.id.share_item -> {
            Toast.makeText(requireContext(), "Share", Toast.LENGTH_SHORT).show()
            true
        }*/
        R.id.like_item -> {
            if(viewModel.isSelectedMovieInDatabase()){
                viewModel.deleteMovieFromDatabase()
                item.icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_empty_favourite_icon, null)
                Toast.makeText(requireContext(), "Removed from favourites", Toast.LENGTH_SHORT).show()
            }else {
                viewModel.addMovieToDatabase()
                item.icon = ResourcesCompat.getDrawable(resources, R.drawable.id_favorite, null)
                Toast.makeText(requireContext(), "Added to favourites", Toast.LENGTH_SHORT).show()
            }
            true
        }
        else -> false


    }
}