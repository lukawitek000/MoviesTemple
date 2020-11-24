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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
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


class PopularMoviesFragment : Fragment(), MoviesAdapter.MovieAdapterOnClickHandler {

    private var moviesAdapter: MoviesAdapter? = null
    private lateinit var viewModel: MainViewModel
    private lateinit var movieRecyclerView: RecyclerView
    private lateinit var errorMessageTextView: TextView
    private lateinit var progressBar: ProgressBar


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_popular_movies, container, false)

        movieRecyclerView = view.findViewById(R.id.popular_movies_recyclerview)
        errorMessageTextView = view.findViewById(R.id.error_message_textview)
        progressBar = view.findViewById(R.id.popular_movies_progressbar)



        setupViewModel()
        viewModel.getPopularMovies()
        setUpRecyclerView()
        setObservers()





        val refresh = view.findViewById<SwipeRefreshLayout>(R.id.swipe_refresh_layout)
        refresh.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(requireContext(), R.color.darkYellow))
        refresh.setColorSchemeColors(Color.BLACK)

        refresh.setOnRefreshListener {
            Toast.makeText(requireContext(), "Refreshed", Toast.LENGTH_SHORT).show()
            viewModel.getPopularMovies()
            refresh.isRefreshing = false
        }

        return view
    }


    private fun setupViewModel() {
        val viewModelFactory = MainViewModelFactory(requireActivity().application)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(MainViewModel::class.java)
    }


    private fun setUpRecyclerView() {
        val spanCount = (activity as MainActivity).calculateSpanCount()
        val layoutManager = GridLayoutManager(requireContext(), spanCount, LinearLayoutManager.VERTICAL, false)
        movieRecyclerView.layoutManager = layoutManager
        movieRecyclerView.setHasFixedSize(true)
        moviesAdapter = MoviesAdapter(this)
        movieRecyclerView.adapter = moviesAdapter
    }



    private fun setObservers(){
        viewModel.popularMovies.observe(viewLifecycleOwner, Observer {
            Log.i("PopularMoviesFragment", "movies observer = $it")
            if(it != null) {
                moviesAdapter?.submitList(it)
            }
        })

        viewModel.popularMoviesStatus.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                when (it) {
                    MainViewModel.Status.SUCCESS -> {
                        progressBar.visibility = View.GONE
                        movieRecyclerView.visibility = View.VISIBLE
                        errorMessageTextView.visibility = View.GONE
                    }
                    MainViewModel.Status.LOADING -> {
                        progressBar.visibility = View.VISIBLE
                        movieRecyclerView.visibility = View.GONE
                        errorMessageTextView.visibility = View.GONE
                    }
                    else -> {
                        progressBar.visibility = View.GONE
                        movieRecyclerView.visibility = View.GONE
                        errorMessageTextView.visibility = View.VISIBLE
                    }

                }
            }
        })

    }




    override fun onClick(movie: Movie) {
        viewModel.selectMovie(movie)
        findNavController().navigate(R.id.action_popularMoviesFragment_to_detailInformationFragment)
        (activity as MainActivity).changeToolbarTitle(resources.getString(R.string.popular_movie_title))
    }
}