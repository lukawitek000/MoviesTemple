package com.example.android.popularmovies.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.popularmovies.MainActivity
import com.example.android.popularmovies.MainViewModel
import com.example.android.popularmovies.MainViewModelFactory
import com.example.android.popularmovies.R
import com.example.android.popularmovies.adapters.MoviesAdapter
import com.example.android.popularmovies.models.Movie


class TopRatedMoviesFragment : Fragment(), MoviesAdapter.MovieAdapterOnClickHandler {

    private lateinit var moviesAdapter: MoviesAdapter
    private lateinit var viewModel: MainViewModel
    private lateinit var moviesRecyclerView: RecyclerView
    private lateinit var errorMessageTextView: TextView
    private lateinit var refreshButton: ImageButton
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_top_rated_movies, container, false)
        moviesRecyclerView = view.findViewById(R.id.top_rated_movies_recyclerview)
        errorMessageTextView = view.findViewById(R.id.error_message_textview)
        refreshButton = view.findViewById(R.id.refresh_button)
        progressBar = view.findViewById(R.id.top_rated_movies_progressbar)
        setUpViewModel()
        viewModel.getTopRatedMovies()
        setUpRecyclerView()
        setUpObservers()

        refreshButton.setOnClickListener {
            viewModel.getTopRatedMovies()
        }

        return view
    }


    private fun setUpViewModel() {
        val viewModelFactory = MainViewModelFactory(requireActivity().application)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(MainViewModel::class.java)
    }

    private fun setUpRecyclerView() {
        val spanCount = (activity as MainActivity).calculateSpanCount()
        val layoutManager = GridLayoutManager(requireContext(), spanCount, LinearLayoutManager.VERTICAL, false)
        moviesRecyclerView.layoutManager = layoutManager
        moviesRecyclerView.setHasFixedSize(true)
        moviesAdapter = MoviesAdapter(this)
        moviesRecyclerView.adapter = moviesAdapter
    }



    private fun setUpObservers() {
        viewModel.topRatedMovies.observe(viewLifecycleOwner, Observer {
            if(it != null){
                moviesAdapter.submitList(it)
            }
        })

        viewModel.topRatedMoviesStatus.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                when (it) {
                    MainViewModel.Status.SUCCESS -> {
                        progressBar.visibility = View.GONE
                        moviesRecyclerView.visibility = View.VISIBLE
                        refreshButton.visibility = View.GONE
                        errorMessageTextView.visibility = View.GONE
                    }
                    MainViewModel.Status.LOADING -> {
                        progressBar.visibility = View.VISIBLE
                        moviesRecyclerView.visibility = View.GONE
                        refreshButton.visibility = View.GONE
                        errorMessageTextView.visibility = View.GONE
                    }
                    else -> {
                        progressBar.visibility = View.GONE
                        moviesRecyclerView.visibility = View.GONE
                        refreshButton.visibility = View.VISIBLE
                        errorMessageTextView.visibility = View.VISIBLE
                    }

                }
            }
        })
    }


    override fun onClick(movie: Movie) {
        viewModel.selectMovie(movie)
        findNavController().navigate(R.id.action_topRatedMoviesFragment_to_detailInformationFragment)
    }


}