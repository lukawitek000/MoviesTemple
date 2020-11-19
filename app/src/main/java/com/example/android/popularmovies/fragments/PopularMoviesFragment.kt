package com.example.android.popularmovies.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
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
import com.example.android.popularmovies.databinding.FragmentMainViewBinding
import com.example.android.popularmovies.models.Movie
import com.example.android.popularmovies.utilities.IMAGE_WIDTH


class PopularMoviesFragment : Fragment(), MoviesAdapter.MovieAdapterOnClickHandler {

    private var movieAdapter: MoviesAdapter? = null

    private lateinit var viewModel: MainViewModel

    private lateinit var binding: FragmentMainViewBinding

    private lateinit var movieRecyclerView: RecyclerView



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_view, container, false)

        movieRecyclerView = binding.recyclerviewMovies

        setupViewModel()
        //viewModel.getPopularMovies()
        setUpRecyclerView()
        setObservers()

       // setHasOptionsMenu(true)
        return binding.root
    }


    private fun setupViewModel() {
        val viewModelFactory = MainViewModelFactory(requireActivity().application)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(MainViewModel::class.java)
    }


    private fun setUpRecyclerView() {
        val spanCount = calculateSpanCount()
        val layoutManager = GridLayoutManager(requireContext(), spanCount, LinearLayoutManager.VERTICAL, false)
        movieRecyclerView.layoutManager = layoutManager
        movieRecyclerView.setHasFixedSize(true)
        movieAdapter = MoviesAdapter(this)
        movieRecyclerView.adapter = movieAdapter
    }


    private fun calculateSpanCount(): Int {
        val displayWidth = resources.displayMetrics.widthPixels
        Log.i("PopularMoviesFragment", "display width : $displayWidth")
        return displayWidth/ IMAGE_WIDTH + 1
    }

    private fun setObservers(){
        viewModel.popularMoviesStatus.observe(viewLifecycleOwner, Observer {
            Log.i("PopularMoviesFragment", "status observer = $it")
            when(it) {
                MainViewModel.Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.failureTextView.visibility = View.GONE
                    movieRecyclerView.visibility = View.GONE
                }
                MainViewModel.Status.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    binding.failureTextView.visibility = View.GONE
                    movieRecyclerView.visibility = View.VISIBLE
                }
                else -> {
                    binding.progressBar.visibility = View.GONE
                    binding.failureTextView.visibility = View.VISIBLE
                    movieRecyclerView.visibility = View.GONE
                }
            }

        })
/*
        viewModel.movies.observe(viewLifecycleOwner, Observer {
            Log.i("PopularMoviesFragment", "movies observer = $it")
            if(it != null) {
                movieAdapter?.submitList(it)
            }
        })
*/
        viewModel.popularMovies.observe(viewLifecycleOwner, Observer {
            Log.i("PopularMoviesFragment", "movies observer = $it")
            if(it != null) {
                movieAdapter?.submitList(it)
            }
        })
        viewModel.databaseValues.observe(viewLifecycleOwner, Observer {
            Log.i("PopularMoviesFragment", "database value $it")
            if(it != null){
                //viewModel.setFavouriteMovies(it)
            }
        })
    }



/*
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.sort_by_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId){
        R.id.sort_by_popularity -> {
            viewModel.setListType(MainViewModel.MovieTypeList.POPULAR_MOVIES)
            true
        }
        R.id.sort_by_votes -> {
            viewModel.setListType(MainViewModel.MovieTypeList.TOP_RATED_MOVIES)
            true
        }
        R.id.show_favourites -> {
            viewModel.setListType(MainViewModel.MovieTypeList.FAVOURITE_MOVIES)
            true
        }
        else -> {
            false
        }
    }*/



    override fun onClick(movie: Movie?) {
        viewModel.selectedMovie = movie
        findNavController().navigate(R.id.action_popularMoviesFragment_to_detailInformationFragment)
    }
}