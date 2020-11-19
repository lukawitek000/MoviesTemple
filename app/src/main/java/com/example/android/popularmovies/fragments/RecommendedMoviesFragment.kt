package com.example.android.popularmovies.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
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
import com.example.android.popularmovies.utilities.IMAGE_WIDTH


class RecommendedMoviesFragment : Fragment(), MoviesAdapter.MovieAdapterOnClickHandler {

    private lateinit var moviesAdapter: MoviesAdapter
    private lateinit var viewModel: MainViewModel
    //private lateinit var progressBar: ProgressBar
    private lateinit var recyclerView: RecyclerView


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_recommended_movies, container, false)
        recyclerView = view.findViewById(R.id.recommended_movies_recyclerview)
        setUpViewModel()
        setUpRecyclerView()
        setUpObservers()
        viewModel.getRecommendationsBasedOnFavouriteMovies()

        return view
    }

    private fun setUpObservers() {
        viewModel.recommendedMovies.observe(viewLifecycleOwner, Observer {
            if(it != null){
                moviesAdapter.submitList(it.toList())
            }
        })
    }

    private fun setUpViewModel() {
        val viewModelFactory = MainViewModelFactory(requireActivity().application)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(MainViewModel::class.java)
    }

    private fun setUpRecyclerView() {
        val spanCount = (activity as MainActivity).calculateSpanCount()
        val layoutManager = GridLayoutManager(requireContext(), spanCount, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        moviesAdapter = MoviesAdapter(this)
        recyclerView.adapter = moviesAdapter
    }



    override fun onClick(movie: Movie) {
        viewModel.selectMovie(movie)
        findNavController().navigate(R.id.action_recommendMoviesFragment_to_detailInformationFragment)
    }


}