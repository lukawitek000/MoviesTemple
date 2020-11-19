package com.example.android.popularmovies.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.example.android.popularmovies.utilities.IMAGE_WIDTH


class FavouriteMoviesFragment : Fragment(), MoviesAdapter.MovieAdapterOnClickHandler{

    private lateinit var moviesAdapter: MoviesAdapter
    private lateinit var viewModel: MainViewModel

    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyFavouriteMoviesListTextView: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        val view = inflater.inflate(R.layout.fragment_favourite_movies, container, false)
        recyclerView = view.findViewById(R.id.favourite_movies_recyclerview)
        emptyFavouriteMoviesListTextView = view.findViewById(R.id.empty_list_favourite_movies_textview)
        setUpViewModel()
        setUpRecyclerView()
        setUpObservers()

        return view
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



    private fun setUpObservers() {
        viewModel.databaseValues.observe(viewLifecycleOwner, Observer {
            Log.i("FavouriteMoviesFragment", "database value $it")
            if(it != null){
                viewModel.setResponseFromDatabaseToFavouriteMovies(it)
                moviesAdapter.submitList(viewModel.getFavouriteMovies())
                if(it.isEmpty()){
                    emptyFavouriteMoviesListTextView.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                }else{
                    emptyFavouriteMoviesListTextView.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                }
            }
        })
    }

    override fun onClick(movie: Movie) {
        viewModel.selectMovie(movie)
        findNavController().navigate(R.id.action_favouriteMoviesFragment_to_detailInformationFragment)
    }

}