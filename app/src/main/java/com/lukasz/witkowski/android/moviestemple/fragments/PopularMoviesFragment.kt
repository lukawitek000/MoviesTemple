package com.lukasz.witkowski.android.moviestemple.fragments

import android.content.Context
import android.os.Bundle
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
        setTextWhenMoviesAdapterIsEmpty("There is no such movies")
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
        job?.cancel()
        job = lifecycleScope.launch {
            sharedViewModel.getMovies(query).collectLatest {
                moviesAdapter.submitData(it)
            }
        }
    }



    override fun onMovieClick(movie: Movie) {
        sharedViewModel.selectMovie(movie)
        findNavController().navigate(R.id.action_popularMoviesFragment_to_detailInformationFragment)
        (activity as MainActivity).changeToolbarTitle(resources.getString(R.string.popular_movie_title))
    }


    private lateinit var searchView: SearchView

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        val menuItem = menu.findItem(R.id.search_icon)
        searchView = menuItem.actionView as SearchView
        searchView.queryHint = resources.getString(R.string.search_hint)
        handleSearchStateToolbarView(searchView, menuItem)
        setQueryListener(searchView)
    }


    private fun handleSearchStateToolbarView(searchView: SearchView, menuItem: MenuItem?) {
        if(sharedViewModel.toolbarState == MainViewModel.ToolbarState.SEARCH){
            menuItem?.expandActionView()
            searchView.isActivated = true
            if(sharedViewModel.isDetailInfoClicked){
                searchViewText = sharedViewModel.currentQueryValue ?: searchViewText
            }
            searchView.setQuery(searchViewText, true)
            hideKeyboard()
            sharedViewModel.currentQueryValue?.let { getMovies(it) }
            sharedViewModel.isDetailInfoClicked = false

        }else{
            getMovies()
        }
    }


    private fun setQueryListener(searchView: SearchView) {
        searchView.setOnQueryTextListener(
                object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        getMovies(query ?: POPULAR_MOVIES_QUERY)
                        searchViewText = query ?: ""
                        hideKeyboard()
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        if(!newText.isNullOrEmpty()){
                            sharedViewModel.toolbarState = MainViewModel.ToolbarState.SEARCH
                        }
                        searchViewText = newText ?: ""
                        return true
                    }
                }
        )

        searchView.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener{
            override fun onViewAttachedToWindow(v: View?) {
            }

            override fun onViewDetachedFromWindow(v: View?) {
                if(!sharedViewModel.isDetailInfoClicked) {
                    if(searchView.query.isNullOrEmpty()) {
                        getMovies()
                        sharedViewModel.toolbarState = MainViewModel.ToolbarState.NORMAL
                        sharedViewModel.currentQueryValue = null
                    }
                }else{
                    searchViewText = searchView.query.toString()
                }
            }

        })

    }


    private fun hideKeyboard(){
        val manager = (activity as MainActivity).getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        manager.hideSoftInputFromWindow((activity as MainActivity).currentFocus?.windowToken, 0)
        if(::searchView.isInitialized) {
            searchView.clearFocus()
        }
    }


    override fun onDestroyView() {
        sharedViewModel.isDetailInfoClicked = false
        hideKeyboard()
        super.onDestroyView()
    }


    companion object{
        const val SEARCH_VIEW_KEY = "SEARCH_VIEW_KEY"
    }

    private var searchViewText = ""

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        searchViewText = savedInstanceState?.getString(SEARCH_VIEW_KEY) ?: ""
        super.onViewStateRestored(savedInstanceState)
    }


    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(SEARCH_VIEW_KEY, searchViewText)
        super.onSaveInstanceState(outState)
    }
}