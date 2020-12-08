package com.lukasz.witkowski.android.moviestemple.fragments

import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lukasz.witkowski.android.moviestemple.MainActivity
import com.lukasz.witkowski.android.moviestemple.viewModels.MainViewModel
import com.lukasz.witkowski.android.moviestemple.viewModels.MainViewModelFactory
import com.lukasz.witkowski.android.moviestemple.R
import com.lukasz.witkowski.android.moviestemple.adapters.MoviesAdapter
import com.lukasz.witkowski.android.moviestemple.adapters.MoviesAdapter.Companion.MOVIE_POSTER
import com.lukasz.witkowski.android.moviestemple.adapters.MoviesLoadStateAdapter
import com.lukasz.witkowski.android.moviestemple.api.IMAGE_WIDTH
import com.lukasz.witkowski.android.moviestemple.databinding.MoviesPosterListLayoutBinding

open class BaseListMoviesFragment : Fragment() {

    protected lateinit var moviesAdapter: MoviesAdapter

    private lateinit var moviesRecyclerView: RecyclerView

    protected val sharedViewModel by activityViewModels<MainViewModel> { MainViewModelFactory(requireActivity().application) }

    protected lateinit var binding: MoviesPosterListLayoutBinding


    protected fun setUpRecyclerView() {
        moviesRecyclerView = binding.moviesRecyclerview
        val spanCount = calculateSpanCount()
        val layoutManager = GridLayoutManager(requireContext(), spanCount, LinearLayoutManager.VERTICAL, false)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup(){
            override fun getSpanSize(position: Int): Int {
                val viewType = moviesAdapter.getItemViewType(position)
                return if(viewType == MOVIE_POSTER) spanCount else 1
            }

        }
        moviesRecyclerView.layoutManager = layoutManager
        moviesRecyclerView.setHasFixedSize(true)
    }

    private fun calculateSpanCount(): Int {
        val displayWidth = resources.displayMetrics.widthPixels
        return displayWidth/ IMAGE_WIDTH + 1
    }


    protected fun retryOrRefreshList(){
        refreshOnSwipe()
        setRetryButtonListener()
    }


    private fun refreshOnSwipe(){

        binding.swipeRefreshLayout.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(requireContext(), R.color.darkYellow))
        binding.swipeRefreshLayout.setColorSchemeColors(Color.BLACK)

        binding.swipeRefreshLayout.setOnRefreshListener {
            Toast.makeText(requireContext(), "Refreshed", Toast.LENGTH_SHORT).show()
            moviesAdapter.retry()
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }


    protected fun initAdapter() {
        moviesAdapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        moviesRecyclerView.adapter = moviesAdapter.withLoadStateHeaderAndFooter(
                footer = MoviesLoadStateAdapter{moviesAdapter.retry()},
                header = MoviesLoadStateAdapter{moviesAdapter.retry()}
        )


        moviesAdapter.addLoadStateListener { loadState ->

            Log.i("Paging", "load state ${loadState.source.refresh}   remote ${loadState.refresh}")
            if(loadState.source.refresh is LoadState.Loading){
                setVisibilityBaseOnStatus(MainViewModel.Status.LOADING, "")
            }else if(loadState.source.refresh is LoadState.NotLoading){
               setVisibilityBaseOnStatus(MainViewModel.Status.SUCCESS, "")
            }else if(loadState.source.refresh is LoadState.Error){
                setVisibilityBaseOnStatus(
                        MainViewModel.Status.FAILURE,
                        "Cannot connect to server, check your favourite movies")
            }


            /*  val errorState = loadState.source.append as? LoadState.Error
                      ?: loadState.source.prepend as? LoadState.Error
                      ?: loadState.append as? LoadState.Error
                      ?: loadState.prepend as? LoadState.Error

              errorState?.let {
                  Toast.makeText(requireContext(), "\uD83D\uDE28 Wooops ${it.error}", Toast.LENGTH_LONG).show()
              }*/
        }



    }

    private fun setRetryButtonListener(){
        binding.retryButton.setOnClickListener {
            moviesAdapter.retry()
        }
    }


    fun setTextWhenFavouriteMoviesIsEmpty(text: String){
        moviesAdapter.addLoadStateListener { loadState ->
            if(loadState.append.endOfPaginationReached){
                if(moviesAdapter.itemCount < 1){
                    setVisibilityBaseOnStatus(MainViewModel.Status.FAILURE, text)
                    binding.retryButton.visibility = View.GONE
                }
            }
        }
    }


    private fun setVisibilityBaseOnStatus(status: MainViewModel.Status, failureMessage: String) {
        binding.moviesRecyclerview.visibility = View.VISIBLE
        when (status) {
            MainViewModel.Status.SUCCESS -> {
                binding.progressbar.visibility = View.GONE
                binding.errorMessageTextview.visibility = View.GONE
                binding.retryButton.visibility = View.GONE
            }
            MainViewModel.Status.LOADING -> {
                binding.progressbar.visibility = View.VISIBLE
                binding.errorMessageTextview.visibility = View.GONE
                binding.retryButton.visibility = View.GONE
            }
            else -> {
                binding.errorMessageTextview.text = failureMessage
                binding.progressbar.visibility = View.GONE
                binding.errorMessageTextview.visibility = View.VISIBLE
                binding.retryButton.visibility = View.VISIBLE
            }
        }
    }

}