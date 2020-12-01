package com.lukasz.witkowski.android.moviestemple.fragments

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.lukasz.witkowski.android.moviestemple.MainActivity
import com.lukasz.witkowski.android.moviestemple.MainViewModel
import com.lukasz.witkowski.android.moviestemple.MainViewModelFactory
import com.lukasz.witkowski.android.moviestemple.R
import com.lukasz.witkowski.android.moviestemple.adapters.CastAdapter
import com.lukasz.witkowski.android.moviestemple.adapters.ReviewsAdapter
import com.lukasz.witkowski.android.moviestemple.adapters.VideosAdapter
import com.lukasz.witkowski.android.moviestemple.adapters.VideosAdapter.VideoClickListener
import com.lukasz.witkowski.android.moviestemple.databinding.ActorCustomDialogBinding
import com.lukasz.witkowski.android.moviestemple.databinding.FragmentDetailInfromationBinding
import com.lukasz.witkowski.android.moviestemple.models.*
import com.squareup.picasso.Picasso


class DetailInformationFragment : Fragment(), VideoClickListener, CastAdapter.CastOnClickListener {

    private lateinit var castAdapter: CastAdapter


    private lateinit var videosAdapter: VideosAdapter

    private lateinit var reviewsAdapter: ReviewsAdapter

    private val shareViewModel by activityViewModels<MainViewModel> { MainViewModelFactory(requireActivity().application) }

    private lateinit var binding: FragmentDetailInfromationBinding

    private lateinit var selectedMovie: Movie

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail_infromation, container, false)

        selectedMovie = shareViewModel.selectedMovie.value!!

        //setIsDetailInformationVisible(false)
        setUpObservers()
        setUpReviewsRecyclerView()
        setUpVideosRecyclerView()
        setUpCastRecyclerView()
        setHasOptionsMenu(true)

        val toolbar = binding.detailInformationToolbar
        binding.appBarLayout.setExpanded(true)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)


        return binding.root
    }

    private fun setUpCastRecyclerView() {
        castAdapter = CastAdapter(this)
        binding.castRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.castRecyclerView.adapter = castAdapter
        binding.castRecyclerView.setHasFixedSize(true)
    }


    private fun setUpObservers() {
        shareViewModel.requestDetailInformationStatus.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                setDataToUI()
                when (it) {
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
                    }
                    else -> {
                        binding.detailInformationLayout.visibility = View.GONE
                        setDetailInformationVisible(false)
                        setMainInformationVisible(false)
                        setProgressBarVisible(true)
                       // setProgressBarsVisibility(View.VISIBLE)
                       // setIsDetailInformationVisible(false)
                    }
                }
            }
        })

        shareViewModel.selectedMovie.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                Log.i("DetailInformation", "observer ${it}")
                videosAdapter.videos = it.videos
                reviewsAdapter.reviews = it.reviews
                castAdapter.setCastAdapterList(it.cast)
                binding.directorTextView.text = it.directors.directorToString()
                binding.writersTextView.text = it.writers.writerToString()
                selectedMovie = it
                Log.i("DetailInformation", "film makers = ${selectedMovie.cast}")
                Glide.with(requireContext())
                        .load(selectedMovie.posterUri)
                        .placeholder(R.drawable.poster_placeholder)
                        .into(binding.toolbarPoster)
                binding.detailInformationToolbar.title = selectedMovie.title
            }
        })
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


    private fun setDataToUI(){
        binding.overview.text = selectedMovie.overview
        binding.originalTitle.text = selectedMovie.originalTitle
        binding.releaseDate.text = selectedMovie.releaseDate
        binding.voteAverageTextview.text = selectedMovie.voteAverage.toString()
        binding.votesNumber.text = selectedMovie.voteCount.toString()
        Log.i("DetailInformation", "genres ${selectedMovie.genres}")
        binding.genres.text = selectedMovie.genres.toText()
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
        Log.i("DetailInformation", "set up recycler view reviews = ${selectedMovie.reviews}")
        reviewsAdapter = ReviewsAdapter()
        binding.recyclerviewReviews.layoutManager = linearLayoutManager
        binding.recyclerviewReviews.adapter = reviewsAdapter
        binding.recyclerviewReviews.setHasFixedSize(true)
    }



    override fun onVideoClicked(video: Video) {
        Toast.makeText(requireContext(), "Open video ${video.name}", Toast.LENGTH_SHORT).show()
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
            if (shareViewModel.isSelectedMovieInDatabase()) {
                item.icon = ResourcesCompat.getDrawable(resources, R.drawable.id_favorite, null)
            } else {
                item.icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_empty_favourite_icon, null)
            }
        }

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId){
        R.id.like_item -> {
            if (shareViewModel.isSelectedMovieInDatabase()) {
                shareViewModel.deleteMovieFromDatabase()
                item.icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_empty_favourite_icon, null)
                Toast.makeText(requireContext(), "Removed from favourites", Toast.LENGTH_SHORT).show()
            } else {
                shareViewModel.addMovieToDatabase()
                item.icon = ResourcesCompat.getDrawable(resources, R.drawable.id_favorite, null)
                Toast.makeText(requireContext(), "Added to favourites", Toast.LENGTH_SHORT).show()
            }
            true
        }
        android.R.id.home -> {
            findNavController().navigateUp()
            true
        }
        else -> false


    }

    override fun onClick(actor: Actor) {
        createAlertDialog(actor).show()
    }


    private fun createAlertDialog(actor: Actor): AlertDialog {
        val builder = AlertDialog.Builder(requireContext())

        val view = layoutInflater.inflate(R.layout.actor_custom_dialog, null)
        val binding = ActorCustomDialogBinding.bind(view)
        builder.setView(view)
        val dialog = builder.create()
       binding.moreInfoButton.setOnClickListener {
            //https://www.themoviedb.org/person/
            //dialog.dismiss()
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://www.themoviedb.org/person/${actor.actorId}")
            startActivity(intent)
        }
        binding.exitIcon.setOnClickListener {
            dialog.dismiss()
        }

        binding.actorName.text = actor.name
        Glide.with(view)
                .load(actor.actorPhoto)
                .placeholder(R.drawable.actor_photo_default)
                .into(binding.actorPhoto)
        return dialog

    }
}