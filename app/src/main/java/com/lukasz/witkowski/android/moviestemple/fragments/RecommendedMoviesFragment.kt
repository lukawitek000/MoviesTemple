package com.lukasz.witkowski.android.moviestemple.fragments

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
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


class RecommendedMoviesFragment : Fragment(), MoviesAdapter.MovieAdapterOnClickHandler {

    private lateinit var moviesAdapter: MoviesAdapter
    private lateinit var viewModel: MainViewModel
    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private lateinit var errorMessageTextView: TextView
    private lateinit var infoTextView: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.fragment_recommended_movies, container, false)
        recyclerView = view.findViewById(R.id.recommended_movies_recyclerview)
        progressBar  = view.findViewById(R.id.recommended_movies_progressbar)
        errorMessageTextView = view.findViewById(R.id.error_message_textview)
        infoTextView = view.findViewById(R.id.recommendations_info_textview)

        setUpViewModel()
        setUpRecyclerView()
        setUpObservers()

        val refresh = view.findViewById<SwipeRefreshLayout>(R.id.swipe_refresh_recommendations_layout)
        refresh.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(requireContext(), R.color.darkYellow))
        refresh.setColorSchemeColors(Color.BLACK)

        refresh.setOnRefreshListener {
            Toast.makeText(requireContext(), "Refreshed", Toast.LENGTH_SHORT).show()
            viewModel.getRecommendationsBasedOnFavouriteMovies()
            refresh.isRefreshing = false
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
                        infoTextView.visibility = View.GONE
                    }
                    MainViewModel.Status.SUCCESS ->{
                        progressBar.visibility = View.GONE
                        recyclerView.visibility = View.VISIBLE
                        errorMessageTextView.visibility = View.GONE
                        if(viewModel.recommendedMovies.value.isNullOrEmpty()){
                            infoTextView.visibility = View.VISIBLE
                        }else {
                            infoTextView.visibility = View.GONE
                        }
                    }
                    else -> {
                        progressBar.visibility = View.GONE
                        recyclerView.visibility = View.GONE
                        errorMessageTextView.visibility = View.VISIBLE
                        infoTextView.visibility = View.GONE
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
        (activity as MainActivity).changeToolbarTitle(resources.getString(R.string.recommended_movies_title))
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
        val view = layoutInflater.inflate(R.layout.recommendations_info_dialog, null)


        val builder = AlertDialog.Builder(requireContext())

        builder.setView(view)
        val dialog = builder.create()
        view.findViewById<Button>(R.id.ok_button).setOnClickListener {
            dialog.dismiss()
        }
        return dialog
    }
}