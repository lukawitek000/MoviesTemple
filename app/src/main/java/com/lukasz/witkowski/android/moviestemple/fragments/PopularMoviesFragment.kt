package com.lukasz.witkowski.android.moviestemple.fragments

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.asFlow
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
        setObservers()
        Log.i("PopularMoviesFragment", "query ${sharedViewModel.currentQueryValue}")
        Log.i("PopularMoviesFragment", "toolbar state ${sharedViewModel.toolbarState.name}")
        if(sharedViewModel.toolbarState == MainViewModel.ToolbarState.SEARCH) {
            getMovies(sharedViewModel.currentQueryValue ?: POPULAR_MOVIES_QUERY)
        }else{
            getMovies()
        }

        setHasOptionsMenu(true)
        return binding.root
    }


    private fun setObservers(){
        /*sharedViewModel.toolbarState.observe(viewLifecycleOwner,  {
            Log.i("PopularMoviesFragment", "observer status $it")
        })*/
    }

    private var job: Job? = null


    private fun getMovies(query: String = POPULAR_MOVIES_QUERY){
        if(query != POPULAR_MOVIES_QUERY){
            sharedViewModel.toolbarState = MainViewModel.ToolbarState.SEARCH
        }

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
        Log.i("PopularMoviesFragment", "on Create OPtions meny")
        if(sharedViewModel.toolbarState == MainViewModel.ToolbarState.SEARCH){
            menuItem.expandActionView()
            searchView.isActivated = true
            searchView.setQuery(sharedViewModel.currentQueryValue, true)
            Log.i("PopularMoviesFragment", "activate search view")
        }
        searchView.setOnQueryTextListener(
                object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        Log.i("PopularMoviesFragment", "on text submit $query")
                        getMovies(query ?: POPULAR_MOVIES_QUERY)
                        hideKeyboard()
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {

                        if(newText.isNullOrEmpty()){
                            Log.i("PopularMoviesFragment", "on text changed $newText")
                            //getMovies()
                        }
                        return true
                    }

                }
        )

    }





    private fun hideKeyboard(){
        val inputMethodManager = requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideKeyboard()
    }
}