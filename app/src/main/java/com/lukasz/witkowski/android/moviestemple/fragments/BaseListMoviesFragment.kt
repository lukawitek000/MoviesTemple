package com.lukasz.witkowski.android.moviestemple.fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.lukasz.witkowski.android.moviestemple.MainActivity
import com.lukasz.witkowski.android.moviestemple.MainViewModel
import com.lukasz.witkowski.android.moviestemple.MainViewModelFactory
import com.lukasz.witkowski.android.moviestemple.R
import com.lukasz.witkowski.android.moviestemple.adapters.MoviesAdapter
import com.lukasz.witkowski.android.moviestemple.adapters.MoviesAdapter.Companion.MOVIE_POSTER
import com.lukasz.witkowski.android.moviestemple.adapters.MoviesLoadStateAdapter

open class BaseListMoviesFragment : Fragment() {

    protected lateinit var moviesAdapter: MoviesAdapter

    protected lateinit var movieRecyclerView: RecyclerView

    protected val sharedViewModel by activityViewModels<MainViewModel> { MainViewModelFactory(requireActivity().application) }




    protected fun setUpRecyclerView() {
        val spanCount = (activity as MainActivity).calculateSpanCount()
        val layoutManager = GridLayoutManager(requireContext(), spanCount, LinearLayoutManager.VERTICAL, false)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup(){
            override fun getSpanSize(position: Int): Int {
                val viewType = moviesAdapter.getItemViewType(position)
                return if(viewType == MOVIE_POSTER) spanCount else 1
            }

        }
        movieRecyclerView.layoutManager = layoutManager
        movieRecyclerView.setHasFixedSize(true)
    }


    protected fun refreshOnSwipe(refresh: SwipeRefreshLayout){
        refresh.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(requireContext(), R.color.darkYellow))
        refresh.setColorSchemeColors(Color.BLACK)

        refresh.setOnRefreshListener {
            Toast.makeText(requireContext(), "Refreshed", Toast.LENGTH_SHORT).show()
            moviesAdapter.retry()
            refresh.isRefreshing = false
        }
    }


    protected fun initAdapter() {
        movieRecyclerView.adapter = moviesAdapter.withLoadStateHeaderAndFooter(
                footer = MoviesLoadStateAdapter{moviesAdapter.retry()},
                header = MoviesLoadStateAdapter{moviesAdapter.retry()}
        )


        moviesAdapter.addLoadStateListener { loadState ->

            Log.i("Paging", "load state ${loadState.source.refresh}   remote ${loadState.refresh}")
            if(loadState.source.refresh is LoadState.Loading){
                (requireActivity() as MainActivity).setVisibilityBaseOnStatus(MainViewModel.Status.LOADING, "")
            }else if(loadState.source.refresh is LoadState.NotLoading){
                (requireActivity() as MainActivity).setVisibilityBaseOnStatus(MainViewModel.Status.SUCCESS, "")
            }else if(loadState.source.refresh is LoadState.Error){
                (requireActivity() as MainActivity).setVisibilityBaseOnStatus(
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

}