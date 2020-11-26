package com.lukasz.witkowski.android.moviestemple.fragments

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.lukasz.witkowski.android.moviestemple.MainActivity
import com.lukasz.witkowski.android.moviestemple.MainViewModel
import com.lukasz.witkowski.android.moviestemple.MainViewModelFactory
import com.lukasz.witkowski.android.moviestemple.R
import com.lukasz.witkowski.android.moviestemple.adapters.MoviesAdapter
import com.lukasz.witkowski.android.moviestemple.models.Movie
import com.lukasz.witkowski.android.moviestemple.viewModels.RecommendedMoviesViewModel
import com.lukasz.witkowski.android.moviestemple.viewModels.RecommendedMoviesViewModelFactory
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class RecommendedMoviesFragment : BaseListMoviesFragment(), MoviesAdapter.MovieAdapterOnClickHandler {


    private val viewModel by viewModels<RecommendedMoviesViewModel> { RecommendedMoviesViewModelFactory(requireActivity().application) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.movies_poster_list_layout, container, false)

        moviesAdapter = MoviesAdapter((this))
        setUpRecyclerView()

        refreshOnSwipe()

        initAdapter()
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId){
        R.id.recommended_movies_info -> {
            buildAlertDialog().show()
            true
        }
        else -> {
            false
        }
    }

    private fun buildAlertDialog(): AlertDialog {
        val view = layoutInflater.inflate(R.layout.recommendations_info_dialog, null)


        val builder = AlertDialog.Builder(requireContext())

        builder.setView(view)
        val dialog = builder.create()
        view.findViewById<Button>(R.id.ok_button).setOnClickListener {
            dialog.dismiss()
        }
        return dialog
    }
}