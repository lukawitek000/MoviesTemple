package com.example.android.popularmovies.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageButton
import androidx.fragment.app.Fragment
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
import com.example.android.popularmovies.utilities.IMAGE_WIDTH


class RecommendedMoviesFragment : Fragment(), MoviesAdapter.MovieAdapterOnClickHandler {

    private lateinit var moviesAdapter: MoviesAdapter
    private lateinit var viewModel: MainViewModel
    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private lateinit var errorMessageTextView: TextView
    private lateinit var refreshButton: ImageButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.fragment_recommended_movies, container, false)
        recyclerView = view.findViewById(R.id.recommended_movies_recyclerview)
        progressBar  = view.findViewById(R.id.recommended_movies_progressbar)
        errorMessageTextView = view.findViewById(R.id.error_message_textview)
        refreshButton = view.findViewById(R.id.refresh_button)

        setUpViewModel()
        setUpRecyclerView()
        setUpObservers()

        refreshButton.setOnClickListener {
            viewModel.getRecommendationsBasedOnFavouriteMovies()
        }

        setHasOptionsMenu(true)
        return view
    }

    private fun setUpObservers() {
        viewModel.recommendedMovies.observe(viewLifecycleOwner, Observer {
            if(it != null){
                moviesAdapter.submitList(it.toList())
            }
        })

        viewModel.databaseValues.observe(viewLifecycleOwner, Observer {
            if(it != null){
                viewModel.setResponseFromDatabaseToFavouriteMovies(it)
                viewModel.getRecommendationsBasedOnFavouriteMovies()
            }
        })

        viewModel.recommendedMoviesStatus.observe(viewLifecycleOwner, Observer {
            if(it != null){
                when(it){
                    MainViewModel.Status.LOADING -> {
                        progressBar.visibility = View.VISIBLE
                        recyclerView.visibility = View.GONE
                        errorMessageTextView.visibility = View.GONE
                        refreshButton.visibility = View.GONE
                    }
                    MainViewModel.Status.SUCCESS ->{
                        progressBar.visibility = View.GONE
                        recyclerView.visibility = View.VISIBLE
                        errorMessageTextView.visibility = View.GONE
                        refreshButton.visibility = View.GONE
                    }
                    else -> {
                        progressBar.visibility = View.GONE
                        recyclerView.visibility = View.GONE
                        errorMessageTextView.visibility = View.VISIBLE
                        refreshButton.visibility = View.VISIBLE
                    }

                }
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


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        return inflater.inflate(R.menu.recommendations_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId){
        R.id.recommended_movies_info -> {
            buildAlertDialog().show()
            true
        }
        else -> {
            false
        }
    }

    private fun buildAlertDialog(): AlertDialog {
        val builder = AlertDialog.Builder(requireContext())
        builder.apply {
            setTitle("Info")
            setMessage("Recommendations are based on your favourite movies")
            setPositiveButton("OK", DialogInterface.OnClickListener { _, _ ->  })
        }
        return builder.create()
    }
}