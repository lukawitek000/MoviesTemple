package com.lukasz.witkowski.android.moviestemple.fragments

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.lukasz.witkowski.android.moviestemple.MainActivity
import com.lukasz.witkowski.android.moviestemple.R
import com.lukasz.witkowski.android.moviestemple.adapters.MoviesAdapter
import com.lukasz.witkowski.android.moviestemple.api.POPULAR_MOVIES_QUERY
import com.lukasz.witkowski.android.moviestemple.models.Movie
import com.lukasz.witkowski.android.moviestemple.viewModels.MainViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class PopularMoviesFragment : BaseListMoviesFragment(), MoviesAdapter.MovieAdapterOnClickHandler {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.movies_poster_list_layout, container, false)
        moviesAdapter = MoviesAdapter(this)
        setUpRecyclerView()
        retryOrRefreshList()
        initAdapter()
        initialGetMovies()
        setHasOptionsMenu(true)
        return binding.root
    }

    private fun initialGetMovies() {
        if(sharedViewModel.toolbarState == MainViewModel.ToolbarState.SEARCH) {
            getMovies(sharedViewModel.currentQueryValue ?: POPULAR_MOVIES_QUERY)
        }else{
            getMovies()
        }
    }


    private var job: Job? = null

    private fun getMovies(query: String = POPULAR_MOVIES_QUERY){
        //setToolbarState(query)

        job?.cancel()
        job = lifecycleScope.launch {
            sharedViewModel.getMovies(query).collectLatest {
                moviesAdapter.submitData(it)
            }
        }
    }


    private fun setToolbarState(query: String) {
        if(query != POPULAR_MOVIES_QUERY){
            sharedViewModel.toolbarState = MainViewModel.ToolbarState.SEARCH
        }else{
            sharedViewModel.toolbarState = MainViewModel.ToolbarState.NORMAL
        }
    }


    override fun onMovieClick(movie: Movie) {
        sharedViewModel.isDetailInfoClicked = true
        sharedViewModel.selectMovie(movie)
        findNavController().navigate(R.id.action_popularMoviesFragment_to_detailInformationFragment)
        (activity as MainActivity).changeToolbarTitle(resources.getString(R.string.popular_movie_title))
    }

    private var hideSearching = true


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        val menuItem = menu.findItem(R.id.search_icon)
        val searchView = menuItem.actionView as SearchView
        Log.i("PopularMoviesFragment", "onCreateOptionsMenu: ${sharedViewModel.toolbarState.name}")
        searchView.queryHint = resources.getString(R.string.search_hint)
        hideSearching = false
        handleSearchStateToolbarView(searchView, menuItem)
        setQueryListener(searchView)

    }


    private fun handleSearchStateToolbarView(searchView: SearchView, menuItem: MenuItem?) {
        if(sharedViewModel.toolbarState == MainViewModel.ToolbarState.SEARCH){
            menuItem?.expandActionView()
            searchView.isActivated = true
            searchView.setQuery(sharedViewModel.currentQueryValue, true)
            Log.i("PopularMoviesFragment", "handleSearchStateToolbarView: ${sharedViewModel.currentQueryValue} ${sharedViewModel.toolbarState.name} ")
            sharedViewModel.currentQueryValue?.let { getMovies(it) }
            //hideSearching = false
            sharedViewModel.isDetailInfoClicked = false
        }
    }


    private fun setQueryListener(searchView: SearchView) {
        searchView.setOnQueryTextListener(
                object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        getMovies(query ?: POPULAR_MOVIES_QUERY)
                      //  sharedViewModel.toolbarState = MainViewModel.ToolbarState.SEARCH
                       // searchView.isActivated = true
                        hideKeyboard()
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        Log.i("PopularMoviesFragment", "onQueryTextChange: $newText ${sharedViewModel.currentQueryValue} ${sharedViewModel.toolbarState.name} ")
                        Log.i("PopularMoviesFragment", "onQueryTextChange:  is searchview activated ${searchView.isActivated} ")

                        Log.i("PopularMoviesFragment", "onQueryTextChange: hidesearching $hideSearching")
                        return true
                    }
                }
        )

        searchView.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener{
            override fun onViewAttachedToWindow(v: View?) {
                Log.i("PopularMoviesFragment", "onAttach")
                sharedViewModel.toolbarState = MainViewModel.ToolbarState.SEARCH
            }

            override fun onViewDetachedFromWindow(v: View?) {
                Log.i("PopularMoviesFragment", "onDetach")
                if(!sharedViewModel.isDetailInfoClicked) {
                    if(searchView.query.isNullOrEmpty()) {
                        getMovies()
                        sharedViewModel.toolbarState = MainViewModel.ToolbarState.NORMAL
                    }
                }
            }

        })
    }


    private fun hideKeyboard(){
        val inputMethodManager = requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        sharedViewModel.isDetailInfoClicked = false
        hideKeyboard()
    }
}