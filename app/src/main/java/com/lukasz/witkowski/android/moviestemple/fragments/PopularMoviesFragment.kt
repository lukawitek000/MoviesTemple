package com.lukasz.witkowski.android.moviestemple.fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
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
import com.lukasz.witkowski.android.moviestemple.adapters.MoviesLoadStateAdapter
import com.lukasz.witkowski.android.moviestemple.models.Movie
import com.lukasz.witkowski.android.moviestemple.viewModels.PopularMoviesViewModel
import com.lukasz.witkowski.android.moviestemple.viewModels.PopularMoviesViewModelFactory
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch


class PopularMoviesFragment : Fragment(), MoviesAdapter.MovieAdapterOnClickHandler {

    private var moviesAdapter: MoviesAdapter = MoviesAdapter(this)

    private lateinit var movieRecyclerView: RecyclerView

    private val sharedViewModel by activityViewModels<MainViewModel> { MainViewModelFactory(requireActivity().application) }
    private val viewModel by viewModels<PopularMoviesViewModel> { (PopularMoviesViewModelFactory(requireActivity().application)) }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.movies_poster_list_layout, container, false)
        movieRecyclerView = view.findViewById(R.id.movies_recyclerview)

        viewModel.getPopularMovies()
        setUpRecyclerView()
        setObservers()





        val refresh = view.findViewById<SwipeRefreshLayout>(R.id.swipe_refresh_layout)
        refresh.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(requireContext(), R.color.darkYellow))
        refresh.setColorSchemeColors(Color.BLACK)

        refresh.setOnRefreshListener {
            Toast.makeText(requireContext(), "Refreshed", Toast.LENGTH_SHORT).show()
            //viewModel.getPopularMovies()
            moviesAdapter.retry()
            refresh.isRefreshing = false
        }

        initAdapter()
        getPopularMovies()
       // initSearch()

        return view
    }


    private var job: Job? = null

    private fun getPopularMovies() {
        job?.cancel()
        job = lifecycleScope.launch {
            viewModel.getPopularMovies().collectLatest {
                moviesAdapter.submitData(it)
            }
        }
    }

    private fun initAdapter() {
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
                (requireActivity() as MainActivity).setVisibilityBaseOnStatus(MainViewModel.Status.FAILURE, "Cannot connect Paging")
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


    private fun setUpRecyclerView() {
        val spanCount = (activity as MainActivity).calculateSpanCount()
        val layoutManager = GridLayoutManager(requireContext(), spanCount, LinearLayoutManager.VERTICAL, false)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup(){
            override fun getSpanSize(position: Int): Int {
                val viewType = moviesAdapter.getItemViewType(position)
                return if(viewType == 1) spanCount else 1
            }

        }
        movieRecyclerView.layoutManager = layoutManager
        movieRecyclerView.setHasFixedSize(true)
       // moviesAdapter = MoviesAdapter(this)
       // movieRecyclerView.adapter = moviesAdapter
    }



    private fun setObservers(){
        viewModel.popularMovies.observe(viewLifecycleOwner, Observer {
            Log.i("PopularMoviesFragment", "movies observer = $it")
            if(it != null) {
               // moviesAdapter?.submitList(it)
            }
        })

        viewModel.popularMoviesStatus.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                (requireActivity() as MainActivity).setVisibilityBaseOnStatus(
                        it,
                        "Cannot connect to server, check your favourite movies")
            }
        })

    }




    override fun onClick(movie: Movie) {
        sharedViewModel.selectMovie(movie)
        findNavController().navigate(R.id.action_popularMoviesFragment_to_detailInformationFragment)
        (activity as MainActivity).changeToolbarTitle(resources.getString(R.string.popular_movie_title))
    }
}