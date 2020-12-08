package com.lukasz.witkowski.android.moviestemple.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.lukasz.witkowski.android.moviestemple.MainActivity
import com.lukasz.witkowski.android.moviestemple.R
import com.lukasz.witkowski.android.moviestemple.adapters.MoviesAdapter
import com.lukasz.witkowski.android.moviestemple.dialogs.RecommendationsInfoDialogFragment
import com.lukasz.witkowski.android.moviestemple.models.Movie
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class RecommendedMoviesFragment : BaseListMoviesFragment(), MoviesAdapter.MovieAdapterOnClickHandler {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.movies_poster_list_layout, container, false)

        moviesAdapter = MoviesAdapter(this)
        setUpRecyclerView()
        retryOrRefreshList()
        initAdapter()
        setTextWhenFavouriteMoviesIsEmpty("Recommendations are based on your favourite movies. \nYou haven't got any yet.")
        getRecommendedMovies()

        setHasOptionsMenu(true)
        return binding.root
    }

    private var job: Job? = null

    private fun getRecommendedMovies() {
        job?.cancel()
        job = lifecycleScope.launch {
            sharedViewModel.getRecommendationsBasedOnFavouriteMovies().collectLatest {
                moviesAdapter.submitData(it)
            }
        }
    }


    override fun onClick(movie: Movie) {
        sharedViewModel.selectMovie(movie)
        findNavController().navigate(R.id.action_recommendMoviesFragment_to_detailInformationFragment)
        (activity as MainActivity).changeToolbarTitle(resources.getString(R.string.recommended_movies_title))
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        return inflater.inflate(R.menu.recommendations_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.recommended_movies_info -> {
            activity?.supportFragmentManager?.let { RecommendationsInfoDialogFragment().show(it, RecommendationsInfoDialogFragment.TAG) }
            true
        }
        else -> {
            false
        }
    }
}
