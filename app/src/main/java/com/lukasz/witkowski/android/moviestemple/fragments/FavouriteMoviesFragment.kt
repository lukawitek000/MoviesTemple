package com.lukasz.witkowski.android.moviestemple.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingData
import com.lukasz.witkowski.android.moviestemple.MainActivity
import com.lukasz.witkowski.android.moviestemple.MainViewModel
import com.lukasz.witkowski.android.moviestemple.R
import com.lukasz.witkowski.android.moviestemple.adapters.MoviesAdapter
import com.lukasz.witkowski.android.moviestemple.models.Movie
import com.lukasz.witkowski.android.moviestemple.models.toMovie
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class FavouriteMoviesFragment : BaseListMoviesFragment(), MoviesAdapter.MovieAdapterOnClickHandler{


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.movies_poster_list_layout, container, false)

        moviesAdapter = MoviesAdapter(this)
        setUpRecyclerView()

        refreshOnSwipe()
        initAdapter()
        setTextWhenFavouriteMoviesIsEmpty("You don't have any favourite movies")
        getFavouriteMovies()

        setHasOptionsMenu(true)
        return binding.root
    }

    private var job: Job? = null

    private fun getFavouriteMovies() {
        job?.cancel()
        job = lifecycleScope.launch {
            sharedViewModel.favouriteMovies.collectLatest { pagingData ->
                val movies: PagingData<Movie> = pagingData.mapSync {
                    it.toMovie()
                }
                moviesAdapter.submitData(movies)
            }
        }
    }





    override fun onClick(movie: Movie) {
        sharedViewModel.selectMovie(movie)
        findNavController().navigate(R.id.action_favouriteMoviesFragment_to_detailInformationFragment)
        (activity as MainActivity).changeToolbarTitle(resources.getString(R.string.favourites_title))
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        return inflater.inflate(R.menu.favourite_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId){
        R.id.delete_all_item -> {
            createAlertDialog().show()
            true
        }
        else -> false
    }

    private fun createAlertDialog(): AlertDialog {
        val builder = AlertDialog.Builder(requireContext())
        val view = layoutInflater.inflate(R.layout.delete_all_custom_dialog, null)

        builder.setView(view)
        val dialog = builder.create()
        view.findViewById<Button>(R.id.delete_all_yes_button).setOnClickListener {
            sharedViewModel.deleteAllFavouriteMovies()
            Toast.makeText(requireContext(), "All favourite movies deleted", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        view.findViewById<Button>(R.id.delete_all_cancel_button).setOnClickListener {
            dialog.dismiss()
        }
        return dialog

    }
}