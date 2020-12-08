package com.lukasz.witkowski.android.moviestemple.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.lukasz.witkowski.android.moviestemple.MainActivity
import com.lukasz.witkowski.android.moviestemple.viewModels.MainViewModel
import com.lukasz.witkowski.android.moviestemple.viewModels.MainViewModelFactory
import com.lukasz.witkowski.android.moviestemple.R
import com.lukasz.witkowski.android.moviestemple.adapters.CastAdapter
import com.lukasz.witkowski.android.moviestemple.adapters.ReviewsAdapter
import com.lukasz.witkowski.android.moviestemple.adapters.VideosAdapter
import com.lukasz.witkowski.android.moviestemple.adapters.VideosAdapter.VideoClickListener
import com.lukasz.witkowski.android.moviestemple.api.PERSON_BASE_URI
import com.lukasz.witkowski.android.moviestemple.databinding.FragmentDetailInfromationBinding
import com.lukasz.witkowski.android.moviestemple.dialogs.ActorDialogFragment
import com.lukasz.witkowski.android.moviestemple.models.*


class DetailInformationFragment : Fragment(), VideoClickListener, CastAdapter.CastOnClickListener {
    
    companion object{
        const val TAG: String = "DetailInformationFragment"
        const val BOTTOM_MENU_STATE = "BOTTOM_MENU_STATE"
    }

    private lateinit var castAdapter: CastAdapter
    private lateinit var videosAdapter: VideosAdapter
    private lateinit var reviewsAdapter: ReviewsAdapter

    private val sharedViewModel by activityViewModels<MainViewModel> { MainViewModelFactory(requireActivity().application) }

    private lateinit var binding: FragmentDetailInfromationBinding

    private lateinit var selectedMovie: Movie
    private var showEnterAnimation = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail_infromation, container, false)

        selectedMovie = sharedViewModel.selectedMovie.value!!

        setUpObservers()
        setUpReviewsRecyclerView()
        setUpVideosRecyclerView()
        setUpCastRecyclerView()
        setHasOptionsMenu(true)
        setUpToolbar()

        if(savedInstanceState != null){
            showEnterAnimation = savedInstanceState.getBoolean(BOTTOM_MENU_STATE)
        }

        if(!showEnterAnimation){
            (requireActivity() as MainActivity).setBottomNavigationVisibility(View.GONE, showEnterAnimation)
        }

        binding.getMoreInfoButton.setOnClickListener {
            sharedViewModel.getMoreInfoForFavouriteMovie()
            binding.nestedScrollView.smoothScrollTo(0, 0)
            binding.appBarLayout.setExpanded(true)
        }

        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(BOTTOM_MENU_STATE, showEnterAnimation)
    }

    private fun setUpToolbar(){
        val toolbar = binding.detailInformationToolbar
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setUpToolbarInfo()
    }

    private fun setUpToolbarInfo() {
        Glide.with(requireContext())
                .load(selectedMovie.posterUri)
                .placeholder(R.drawable.poster_placeholder)
                .into(binding.toolbarPoster)
        binding.detailInformationToolbar.title = selectedMovie.title
    }


    private fun setUpCastRecyclerView() {
        castAdapter = CastAdapter(this)
        binding.castRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.castRecyclerView.adapter = castAdapter
        binding.castRecyclerView.setHasFixedSize(true)
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
        reviewsAdapter = ReviewsAdapter()
        binding.recyclerviewReviews.layoutManager = linearLayoutManager
        binding.recyclerviewReviews.adapter = reviewsAdapter
        binding.recyclerviewReviews.setHasFixedSize(true)
    }


    override fun onVideoClicked(video: Video) {
        Toast.makeText(requireContext(), resources.getString(R.string.open_video_info, video.name), Toast.LENGTH_SHORT).show()
        val intent = Intent(Intent.ACTION_VIEW)
        if(video.site == "YouTube") {
            intent.data = video.videoUri
            startActivity(intent)
        }else{
            Toast.makeText(requireContext(), resources.getString(R.string.unknown_site_info), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActorClick(actor: Actor) {
        activity?.supportFragmentManager?.let { createActorInfoDialog(actor).show(it, ActorDialogFragment.TAG) }
    }


    private fun createActorInfoDialog(actor: Actor): ActorDialogFragment {
        return ActorDialogFragment.newInstance(actor.name, actor.actorPhoto?.toString(),
                PERSON_BASE_URI + actor.actorId)
    }


    private fun setUpObservers() {
        sharedViewModel.requestDetailInformationStatus.observe(viewLifecycleOwner,  {
            if (it != null) {
                setViewsVisibility(it)
            }
        })
        sharedViewModel.selectedMovie.observe(viewLifecycleOwner,  {
            if (it != null) {
                setDataToUI(it)
                selectedMovie = it
                showGetMoreInfoButton()
            }
        })
    }

    private fun setViewsVisibility(status: MainViewModel.Status) {
        when (status) {
            MainViewModel.Status.SUCCESS -> {
                setDetailInformationVisible(true)
                setMainInformationVisible(true)
                setProgressBarVisible(false)
                binding.detailInformationLayout.visibility = View.VISIBLE
            }
            MainViewModel.Status.FAILURE -> {
                binding.detailInformationLayout.visibility = View.GONE
                setDetailInformationVisible(false)
                setMainInformationVisible(true)
                setProgressBarVisible(false)
                if(!isFullInformationInFavouriteMovie()){
                    Toast.makeText(requireContext(),
                            resources.getString(R.string.check_internet_connection_info),
                            Toast.LENGTH_SHORT).show()
                }
            }
            else -> {
                binding.detailInformationLayout.visibility = View.GONE
                setDetailInformationVisible(false)
                setMainInformationVisible(false)
                setProgressBarVisible(true)
            }
        }
    }


    private fun showGetMoreInfoButton() {
        if(!isFullInformationInFavouriteMovie()){
            binding.getMoreInfoButton.visibility = View.VISIBLE
        }else{
            binding.getMoreInfoButton.visibility = View.GONE
        }
    }


    private fun isFullInformationInFavouriteMovie(): Boolean {
        if(selectedMovie.genres.isNullOrEmpty() && selectedMovie.cast.isNullOrEmpty()
                && selectedMovie.directors.isNullOrEmpty() && selectedMovie.writers.isNullOrEmpty()
                && selectedMovie.videos.isNullOrEmpty() && selectedMovie.reviews.isNullOrEmpty()){
                    return false
        }
        return true
    }


    private fun setProgressBarVisible(isVisible: Boolean) {
        if(isVisible){
            binding.progressBar.visibility = View.VISIBLE
        }else{
            binding.progressBar.visibility = View.GONE
        }
    }


    private fun setMainInformationVisible(isVisible: Boolean) {
        if(isVisible){
            binding.detailInformationLayout.visibility = View.VISIBLE
        }else{
            binding.detailInformationLayout.visibility = View.GONE
        }
    }


    private fun setDetailInformationVisible(isVisible: Boolean) {
        if(!isVisible){
            binding.genreLabel.visibility = View.GONE
            binding.genres.visibility = View.GONE
            binding.directorLabel.visibility = View.GONE
            binding.directorTextView.visibility = View.GONE
            binding.writersLabel.visibility = View.GONE
            binding.writersTextView.visibility = View.GONE
            binding.castLabel.visibility = View.GONE
            binding.castRecyclerView.visibility = View.GONE
            binding.videosLabel.visibility = View.GONE
            binding.recyclerviewVideos.visibility = View.GONE
            binding.reviewsLabel.visibility = View.GONE
            binding.recyclerviewReviews.visibility = View.GONE
        }else{
            binding.genreLabel.visibility = checkListIfIsEmpty(selectedMovie.genres)
            binding.genres.visibility = checkListIfIsEmpty(selectedMovie.genres)
            binding.directorLabel.visibility = checkListIfIsEmpty(selectedMovie.directors)
            binding.directorTextView.visibility = checkListIfIsEmpty(selectedMovie.directors)
            binding.writersLabel.visibility = checkListIfIsEmpty(selectedMovie.writers)
            binding.writersTextView.visibility = checkListIfIsEmpty(selectedMovie.writers)
            binding.castLabel.visibility = checkListIfIsEmpty(selectedMovie.cast)
            binding.castRecyclerView.visibility = checkListIfIsEmpty(selectedMovie.cast)
            binding.videosLabel.visibility = checkListIfIsEmpty(selectedMovie.videos)
            binding.recyclerviewVideos.visibility = checkListIfIsEmpty(selectedMovie.videos)
            binding.reviewsLabel.visibility = checkListIfIsEmpty(selectedMovie.reviews)
            binding.recyclerviewReviews.visibility = checkListIfIsEmpty(selectedMovie.reviews)
        }
    }

    private fun <T> checkListIfIsEmpty(list: List<T>): Int{
        return if(list.isNullOrEmpty()){
            View.GONE
        }else{
            View.VISIBLE
        }
    }


    private fun setDataToUI(movie: Movie){
        binding.overview.text = movie.overview
        binding.originalTitle.text = movie.originalTitle
        binding.releaseDate.text = movie.releaseDate
        binding.voteAverageTextview.text = movie.voteAverage.toString()
        binding.votesNumber.text = movie.voteCount.toString()
        binding.genres.text = movie.genres.toText()
        videosAdapter.videos = movie.videos
        reviewsAdapter.reviews = movie.reviews
        castAdapter.setCastAdapterList(movie.cast)
        binding.directorTextView.text = movie.directors.toText()
        binding.writersTextView.text = movie.writers.toText()
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.detail_menu, menu)
        val item = menu.findItem(R.id.like_item)
        if(item != null) {
            if (sharedViewModel.isSelectedMovieInDatabase()) {
                item.icon = ResourcesCompat.getDrawable(resources, R.drawable.id_favorite, null)
            } else {
                item.icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_empty_favourite_icon, null)
            }
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId){
        R.id.like_item -> {
            handleLikeItemClick(item)
            true
        }
        android.R.id.home -> {
            findNavController().navigateUp()
            true
        }
        else -> false
    }


    private fun handleLikeItemClick(item: MenuItem) {
        if (sharedViewModel.isSelectedMovieInDatabase()) {
            sharedViewModel.deleteMovieFromDatabase()
            item.icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_empty_favourite_icon, null)
            Toast.makeText(requireContext(),
                    resources.getString(R.string.removed_from_favourite_info),
                    Toast.LENGTH_SHORT).show()
        } else {
            sharedViewModel.addMovieToDatabase()
            item.icon = ResourcesCompat.getDrawable(resources, R.drawable.id_favorite, null)
            Toast.makeText(requireContext(),
                    resources.getString(R.string.added_to_favourite_info),
                    Toast.LENGTH_SHORT).show()
        }
    }


    override fun onStop() {
        (requireActivity() as MainActivity).setBottomNavigationVisibility(View.VISIBLE, true)
        super.onStop()
    }

    override fun onStart() {
        super.onStart()
        (requireActivity() as MainActivity).setBottomNavigationVisibility(View.GONE, showEnterAnimation)
        showEnterAnimation = false
    }
}