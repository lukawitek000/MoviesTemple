package com.lukasz.witkowski.android.moviestemple.fragments

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.lukasz.witkowski.android.moviestemple.MainActivity
import com.lukasz.witkowski.android.moviestemple.R
import com.lukasz.witkowski.android.moviestemple.adapters.MoviesAdapter
import com.lukasz.witkowski.android.moviestemple.api.POPULAR_MOVIES_QUERY
import com.lukasz.witkowski.android.moviestemple.models.Movie
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class PopularMoviesFragment : BaseListMoviesFragment(), MoviesAdapter.MovieAdapterOnClickHandler {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.movies_poster_list_layout, container, false)
        moviesAdapter = MoviesAdapter(this)
        setUpRecyclerView()
        refreshOnSwipe()
        initAdapter()
        getMovies();
        setHasOptionsMenu(true)
        return binding.root
    }


    private var job: Job? = null


    private fun getMovies(query: String = POPULAR_MOVIES_QUERY){
        job?.cancel()
        job = lifecycleScope.launch {
            sharedViewModel.getMovies(query).collectLatest {
                moviesAdapter.submitData(it)
            }
        }
    }


    override fun onClick(movie: Movie) {
        sharedViewModel.selectMovie(movie)
        findNavController().navigate(R.id.action_popularMoviesFragment_to_detailInformationFragment)
        (activity as MainActivity).changeToolbarTitle(resources.getString(R.string.popular_movie_title))
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        val menuItem = menu.findItem(R.id.search_icon)
        val searchView = menuItem.actionView as SearchView
        searchView.queryHint = "Search Here"
        searchView.setOnQueryTextListener(
                object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        getMovies(query ?: POPULAR_MOVIES_QUERY)
                        val inputMethodManager = requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        Log.i("SearchTest", "on text changed $newText")
                        if(newText.isNullOrEmpty()){
                            getMovies()
                        }
                        return true
                    }

                }
        )
    }

}