package com.example.android.popularmovies.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.popularmovies.MainViewModel
import com.example.android.popularmovies.MainViewModelFactory
import com.example.android.popularmovies.R
import com.example.android.popularmovies.adapters.MoviesAdapter
import com.example.android.popularmovies.models.Movie
import com.example.android.popularmovies.utilities.IMAGE_WIDTH


class TopRatedMoviesFragment : Fragment(), MoviesAdapter.MovieAdapterOnClickHandler {

    private lateinit var moviesAdapter: MoviesAdapter
    private lateinit var viewModel: MainViewModel
    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_top_rated_movies, container, false)
        recyclerView = view.findViewById(R.id.top_rated_movies_recyclerview)
        progressBar = view.findViewById(R.id.top_rated_movies_progress_bar)
        setUpViewModel()
        viewModel.getTopRatedMovies()
        setUpRecyclerView()
        setUpObservers()


        return view
    }


    private fun setUpViewModel() {
        val viewModelFactory = MainViewModelFactory(requireActivity().application)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(MainViewModel::class.java)
    }

    private fun setUpRecyclerView() {
        val spanCount = calculateSpanCount()
        val layoutManager = GridLayoutManager(requireContext(), spanCount, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        moviesAdapter = MoviesAdapter(this)
        recyclerView.adapter = moviesAdapter
    }


    private fun calculateSpanCount(): Int {
        val displayWidth = resources.displayMetrics.widthPixels
        Log.i("TopRatedMoviesFragment", "display width : $displayWidth")
        return displayWidth / IMAGE_WIDTH + 1
    }

    private fun setUpObservers() {
        viewModel.topRatedMoviesStatus.observe(viewLifecycleOwner, Observer {
            Log.i("TopRatedMoviesFragment", "status observer = $it")
            when(it) {
                MainViewModel.Status.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                   // binding.failureTextView.visibility = View.GONE
                    recyclerView.visibility = View.GONE
                }
                MainViewModel.Status.SUCCESS -> {
                    progressBar.visibility = View.GONE
                   // binding.failureTextView.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                }
                else -> {
                    progressBar.visibility = View.GONE
                   // binding.failureTextView.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                }
            }
        })

        viewModel.topRatedMovies.observe(viewLifecycleOwner, Observer {
            if(it != null){
                moviesAdapter.submitList(it)
            }
        })
    }



    override fun onClick(movie: Movie?) {
        viewModel.selectedMovie = movie
        findNavController().navigate(R.id.action_topRatedMoviesFragment_to_detailInformationFragment)
    }


}