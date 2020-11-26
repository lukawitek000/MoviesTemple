package com.lukasz.witkowski.android.moviestemple.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
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

        binding = DataBindingUtil.inflate(inflater, R.layout.movies_poster_list_layout, container, false)
        moviesAdapter = MoviesAdapter(this)
        setUpRecyclerView()
        refreshOnSwipe()

        initAdapter()
        getTopRatedMovies()


        return binding.root
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