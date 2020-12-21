package com.lukasz.witkowski.android.moviestemple.ui.fragments

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
import com.lukasz.witkowski.android.moviestemple.ui.MainActivity
import com.lukasz.witkowski.android.moviestemple.viewModels.MainViewModel
import com.lukasz.witkowski.android.moviestemple.R
import com.lukasz.witkowski.android.moviestemple.ui.adapters.CastAdapter
import com.lukasz.witkowski.android.moviestemple.ui.adapters.ReviewsAdapter
import com.lukasz.witkowski.android.moviestemple.ui.adapters.VideosAdapter
import com.lukasz.witkowski.android.moviestemple.ui.adapters.VideosAdapter.VideoClickListener
import com.lukasz.witkowski.android.moviestemple.api.PERSON_BASE_URI
import com.lukasz.witkowski.android.moviestemple.databinding.FragmentDetailInfromationBinding
import com.lukasz.witkowski.android.moviestemple.ui.dialogs.ActorDialogFragment
import com.lukasz.witkowski.android.moviestemple.models.*
import com.lukasz.witkowski.android.moviestemple.util.toText
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailInformationFragment : Fragment(), VideoClickListener, CastAdapter.CastOnClickListener {

    companion object{
        const val TAG: String = "DetailInformationFragment"
        const val BOTTOM_MENU_STATE = "BOTTOM_MENU_STATE"
    }

    private lateinit var castAdapter: CastAdapter
    private lateinit var videosAdapter: VideosAdapter
    private lateinit var reviewsAdapter: ReviewsAdapter

    private val sharedViewModel: MainViewModel by activityViewModels()

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

        binding.btGetMoreInfo.setOnClickListener {
            sharedViewModel.getMoreInfoForFavouriteMovie()
            binding.nsvDetailInformation.smoothScrollTo(0, 0)
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
        if(selectedMovie.posterUri != null){
            Glide.with(requireContext())
                    .load(selectedMovie.posterUri)
                    .placeholder(R.drawable.poster_placeholder)
                    .into(binding.ivToolbarPoster)
        }else{
            Glide.with(requireContext())
                    .load(R.drawable.default_movie_poster)
                    .placeholder(R.drawable.poster_placeholder)
                    .into(binding.ivToolbarPoster)
        }

        binding.detailInformationToolbar.title = selectedMovie.title
    }


    private fun setUpCastRecyclerView() {
        castAdapter = CastAdapter(this)
        binding.rvCast.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvCast.adapter = castAdapter
        binding.rvCast.setHasFixedSize(true)
    }

    private fun setUpVideosRecyclerView() {
        val videoManager =  LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        videosAdapter =  VideosAdapter(this)
        binding.rvVideos.layoutManager = videoManager
        binding.rvVideos.adapter = videosAdapter
        binding.rvVideos.setHasFixedSize(true)
    }


    private fun setUpReviewsRecyclerView() {
        val  linearLayoutManager =  LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        reviewsAdapter = ReviewsAdapter()
        binding.rvReviews.layoutManager = linearLayoutManager
        binding.rvReviews.adapter = reviewsAdapter
        binding.rvReviews.setHasFixedSize(true)
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
                binding.clDetailInformation.visibility = View.VISIBLE
            }
            MainViewModel.Status.FAILURE -> {
                binding.clDetailInformation.visibility = View.GONE
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
                binding.clDetailInformation.visibility = View.GONE
                setDetailInformationVisible(false)
                setMainInformationVisible(false)
                setProgressBarVisible(true)
            }
        }
    }


    private fun showGetMoreInfoButton() {
        if(!isFullInformationInFavouriteMovie()){
            binding.btGetMoreInfo.visibility = View.VISIBLE
        }else{
            binding.btGetMoreInfo.visibility = View.GONE
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
            binding.pbDetailInformation.visibility = View.VISIBLE
        }else{
            binding.pbDetailInformation.visibility = View.GONE
        }
    }


    private fun setMainInformationVisible(isVisible: Boolean) {
        if(isVisible){
            binding.clDetailInformation.visibility = View.VISIBLE
        }else{
            binding.clDetailInformation.visibility = View.GONE
        }
    }


    private fun setDetailInformationVisible(isVisible: Boolean) {
        binding.tvOriginalTitle.visibility = setVisibility(selectedMovie.originalTitle)
        binding.tvOriginalTitleLabel.visibility = setVisibility(selectedMovie.originalTitle)
        binding.tvReleaseDate.visibility = setVisibility(selectedMovie.releaseDate)
        binding.tvReleaseDateLabel.visibility = setVisibility(selectedMovie.releaseDate)
        binding.tvOverview.visibility = setVisibility(selectedMovie.overview)
        binding.tvOverviewLabel.visibility = setVisibility(selectedMovie.overview)
        binding.tvGenreLabel.visibility = setListVisibility(selectedMovie.genres, isVisible)
        binding.tvGenres.visibility = setListVisibility(selectedMovie.genres, isVisible)
        binding.tvDirectorLabel.visibility = setListVisibility(selectedMovie.directors, isVisible)
        binding.tvDirectors.visibility = setListVisibility(selectedMovie.directors, isVisible)
        binding.tvWritersLabel.visibility = setListVisibility(selectedMovie.writers, isVisible)
        binding.tvWriters.visibility = setListVisibility(selectedMovie.writers, isVisible)
        binding.tvCastLabel.visibility = setListVisibility(selectedMovie.cast, isVisible)
        binding.rvCast.visibility = setListVisibility(selectedMovie.cast, isVisible)
        binding.tvVideosLabel.visibility = setListVisibility(selectedMovie.videos, isVisible)
        binding.rvVideos.visibility = setListVisibility(selectedMovie.videos, isVisible)
        binding.tvReviewsLabel.visibility = setListVisibility(selectedMovie.reviews, isVisible)
        binding.rvReviews.visibility = setListVisibility(selectedMovie.reviews, isVisible)
    }

    private fun setVisibility(value: String): Int{
        return if(value.isEmpty()){
            View.GONE
        }else{
            View.VISIBLE
        }
    }


    private fun <T> setListVisibility(list: List<T>, visible: Boolean): Int {
        return if(!visible){
            View.GONE
        }else{
            checkListIfIsEmpty(list)
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
        binding.tvOverview.text = movie.overview
        binding.tvOriginalTitle.text = movie.originalTitle
        binding.tvReleaseDate.text = movie.releaseDate
        binding.tvVoteAverage.text = movie.voteAverage.toString()
        binding.tvVotesNumber.text = movie.voteCount.toString()
        binding.tvGenres.text = movie.genres.toText()
        videosAdapter.videos = movie.videos
        reviewsAdapter.reviews = movie.reviews
        castAdapter.setCastAdapterList(movie.cast)
        binding.tvDirectors.text = movie.directors.toText()
        binding.tvWriters.text = movie.writers.toText()
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.detail_menu, menu)
        val item = menu.findItem(R.id.like_item)
        if(item != null) {
            if (sharedViewModel.isSelectedMovieInDatabase()) {
                item.icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_favorite, null)
            } else {
                item.icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_empty_favourite, null)
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
            item.icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_empty_favourite, null)
            Toast.makeText(requireContext(),
                    resources.getString(R.string.removed_from_favourite_info),
                    Toast.LENGTH_SHORT).show()
        } else {
            sharedViewModel.addMovieToDatabase()
            item.icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_favorite, null)
            Toast.makeText(requireContext(),
                    resources.getString(R.string.added_to_favourite_info),
                    Toast.LENGTH_SHORT).show()
        }
    }


    override fun onStop() {
        sharedViewModel.isDetailInfoClicked = true
        (requireActivity() as MainActivity).setBottomNavigationVisibility(View.VISIBLE, true)
        super.onStop()
    }

    override fun onStart() {
        super.onStart()
        (requireActivity() as MainActivity).setBottomNavigationVisibility(View.GONE, showEnterAnimation)
        showEnterAnimation = false
    }
}