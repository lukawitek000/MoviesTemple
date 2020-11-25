package com.lukasz.witkowski.android.moviestemple.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.lukasz.witkowski.android.moviestemple.MainActivity
import com.lukasz.witkowski.android.moviestemple.R
import com.lukasz.witkowski.android.moviestemple.adapters.MoviesAdapter
import com.lukasz.witkowski.android.moviestemple.models.Movie
import com.lukasz.witkowski.android.moviestemple.viewModels.TopRatedMoviesViewModel
import com.lukasz.witkowski.android.moviestemple.viewModels.TopRatedMoviesViewModelFactory
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class TopRatedMoviesFragment : BaseListMoviesFragment(), MoviesAdapter.MovieAdapterOnClickHandler {

    private val viewModel by viewModels<TopRatedMoviesViewModel> { TopRatedMoviesViewModelFactory(requireActivity().application) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.movies_poster_list_layout, container, false)
        moviesRecyclerView = view.findViewById(R.id.movies_recyclerview)


        moviesAdapter = MoviesAdapter(this)
        setUpRecyclerView()

        val refresh = view.findViewById<SwipeRefreshLayout>(R.id.swipe_refresh_layout)
        refreshOnSwipe(refresh)

        initAdapter()
        getTopRatedMovies()


        return view
    }

    private var job: Job? = null

    private fun getTopRatedMovies(){
        job?.cancel()
        job = lifecycleScope.launch {
            viewModel.getTopRatedMovies().collectLatest{
                moviesAdapter.submitData(it)
            }
        }
    }



    override fun onClick(movie: Movie) {
        sharedViewModel.selectMovie(movie)
        findNavController().navigate(R.id.action_topRatedMoviesFragment_to_detailInformationFragment)
        (activity as MainActivity).changeToolbarTitle(resources.getString(R.string.top_rated_title))
    }


}