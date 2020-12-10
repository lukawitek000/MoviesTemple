package com.lukasz.witkowski.android.moviestemple.fragments

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
import androidx.recyclerview.widget.SimpleItemAnimator
import com.lukasz.witkowski.android.moviestemple.R
import com.lukasz.witkowski.android.moviestemple.adapters.MoviesAdapter
import com.lukasz.witkowski.android.moviestemple.adapters.MoviesAdapter.Companion.MOVIE_POSTER
import com.lukasz.witkowski.android.moviestemple.adapters.MoviesLoadStateAdapter
import com.lukasz.witkowski.android.moviestemple.api.IMAGE_WIDTH
import com.lukasz.witkowski.android.moviestemple.databinding.MoviesPosterListLayoutBinding
import com.lukasz.witkowski.android.moviestemple.viewModels.MainViewModel
import com.lukasz.witkowski.android.moviestemple.viewModels.MainViewModelFactory

open class BaseListMoviesFragment : Fragment() {

    protected lateinit var moviesAdapter: MoviesAdapter
    private lateinit var moviesRecyclerView: RecyclerView
    protected val sharedViewModel by activityViewModels<MainViewModel> { MainViewModelFactory(requireActivity().application) }
    protected lateinit var binding: MoviesPosterListLayoutBinding

    protected fun setUpRecyclerView() {
        moviesRecyclerView = binding.rvMovies
        val spanCount = calculateSpanCount()
        val layoutManager = GridLayoutManager(requireContext(), spanCount, LinearLayoutManager.VERTICAL, false)
        matchGridSpan(layoutManager, spanCount)
        moviesRecyclerView.layoutManager = layoutManager
        moviesRecyclerView.setHasFixedSize(true)
    }


    private fun matchGridSpan(layoutManager: GridLayoutManager, spanCount: Int) {
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup(){
            override fun getSpanSize(position: Int): Int {
                val viewType = moviesAdapter.getItemViewType(position)
                return if(viewType == MOVIE_POSTER) spanCount else 1
            }
        }
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
        binding.swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(requireContext(), R.color.darkGrey))
        binding.swipeRefreshLayout.setOnRefreshListener {
            Toast.makeText(requireContext(), resources.getString(R.string.refreshed_info), Toast.LENGTH_SHORT).show()
            moviesAdapter.retry()
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }


    protected fun initAdapter() {
        moviesAdapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        moviesRecyclerView.adapter = moviesAdapter.withLoadStateHeaderAndFooter(
                footer = MoviesLoadStateAdapter(requireContext()) { moviesAdapter.retry() },
                header = MoviesLoadStateAdapter(requireContext()) { moviesAdapter.retry() }
        )

        moviesAdapter.addLoadStateListener { loadState ->
            when(loadState.refresh){
                is LoadState.Loading -> setVisibilityBaseOnStatus(MainViewModel.Status.LOADING, "")
                is LoadState.NotLoading -> setVisibilityBaseOnStatus(MainViewModel.Status.SUCCESS, "")
                is LoadState.Error -> setVisibilityBaseOnStatus(
                        MainViewModel.Status.FAILURE,
                        resources.getString(R.string.cannot_connect_to_server_info))
            }
        }
    }

    private fun setRetryButtonListener(){
        binding.btRetry.setOnClickListener {
            moviesAdapter.retry()
        }
    }


    fun setTextWhenMoviesAdapterIsEmpty(text: String){
        moviesAdapter.addLoadStateListener { loadState ->
            if(loadState.append.endOfPaginationReached){
                if(moviesAdapter.itemCount < 1){
                    setVisibilityBaseOnStatus(MainViewModel.Status.FAILURE, text)
                    binding.btRetry.visibility = View.GONE
                }
            }
        }
    }


    private fun setVisibilityBaseOnStatus(status: MainViewModel.Status, failureMessage: String) {
        when (status) {
            MainViewModel.Status.SUCCESS -> {
                binding.pbMoviesList.visibility = View.GONE
                binding.tvMoviesListErrorMessage.visibility = View.GONE
                binding.btRetry.visibility = View.GONE
                binding.rvMovies.visibility = View.VISIBLE
                if(sharedViewModel.toolbarState == MainViewModel.ToolbarState.SEARCH){
                    binding.swipeRefreshLayout.isRefreshing = false
                }
            }
            MainViewModel.Status.LOADING -> {
                binding.pbMoviesList.visibility = View.VISIBLE
                binding.tvMoviesListErrorMessage.visibility = View.GONE
                binding.btRetry.visibility = View.GONE
                binding.rvMovies.visibility = View.VISIBLE
                if(sharedViewModel.toolbarState == MainViewModel.ToolbarState.SEARCH){
                    binding.swipeRefreshLayout.isRefreshing = true
                    binding.pbMoviesList.visibility = View.INVISIBLE
                }
            }
            else -> {
                binding.tvMoviesListErrorMessage.text = failureMessage
                binding.pbMoviesList.visibility = View.GONE
                binding.tvMoviesListErrorMessage.visibility = View.VISIBLE
                binding.btRetry.visibility = View.VISIBLE
                if(sharedViewModel.toolbarState == MainViewModel.ToolbarState.SEARCH){
                    binding.rvMovies.visibility = View.INVISIBLE
                    binding.swipeRefreshLayout.isRefreshing = false
                }else{
                    binding.rvMovies.visibility = View.VISIBLE
                }
            }
        }
    }

}