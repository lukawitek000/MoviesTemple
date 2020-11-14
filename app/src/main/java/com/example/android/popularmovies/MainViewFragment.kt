package com.example.android.popularmovies

import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.popularmovies.adapters.MoviesAdapter
import com.example.android.popularmovies.databinding.FragmentMainViewBinding
import com.example.android.popularmovies.models.Movie


class MainViewFragment : Fragment(), MoviesAdapter.MovieAdapterOnClickHandler {

    companion object {
        private const val SELECTED_SORTING = "SELECTED_SORTING"
        const val MOVIE_KEY = "MOVIE"
        const val TITLE_KEY = "TITLE"
        private const val FAVOURITES = "favourites"
        private const val TOP_RATED = "top_rated"
        private const val POPULAR = "popular"
        private const val IMAGE_WIDTH = 342

        fun newInstance(): MainViewFragment{
            return MainViewFragment()
        }
    }
    private var movieAdapter: MoviesAdapter? = null

    private lateinit var viewModel: MainViewModel

    private lateinit var binding: FragmentMainViewBinding

    private lateinit var movieRecyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate<FragmentMainViewBinding>(inflater, R.layout.fragment_main_view, container, false)

        movieRecyclerView = binding.recyclerviewMovies

        setupViewModel()
        setUpRecyclerView()


        binding.progressBar.visibility = View.VISIBLE
        binding.failureTextView.visibility = View.GONE
        movieRecyclerView.visibility = View.GONE

        viewModel.status.observe(viewLifecycleOwner, Observer {
           Log.i("MainActivity", "status observer = $it")
           if(it == MainViewModel.Status.LOADING){
               binding.progressBar.visibility = View.VISIBLE
               binding.failureTextView.visibility = View.GONE
               movieRecyclerView.visibility = View.GONE
           }else if(it == MainViewModel.Status.SUCCESS){
               binding.progressBar.visibility = View.GONE
               binding.failureTextView.visibility = View.GONE
               movieRecyclerView.visibility = View.VISIBLE
           }else{
               binding.progressBar.visibility = View.GONE
               binding.failureTextView.visibility = View.VISIBLE
               movieRecyclerView.visibility = View.GONE
           }


       })

       viewModel.movies.observe(viewLifecycleOwner, Observer {
           Log.i("MainActivity", "movies observer = $it")
           if(it != null) {
               movieAdapter?.setMoviesData(it)
           }
       })

        setHasOptionsMenu(true)
        return binding.root



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
        val point = Point()
        val display = activity?.windowManager?.defaultDisplay
        display?.getSize(point)
        val display_width = point.x
        return Math.round(display_width.toFloat() / IMAGE_WIDTH)
    }

    private fun setupViewModel() {
        val viewModelFactory = MainViewModelFactory(requireActivity().application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.sort_by_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId){
        R.id.sort_by_popularity -> {
            viewModel.setListType(MainViewModel.MovieTypeList.POPULAR_MOVIES)
            //viewModel.listType = MainViewModel.MovieTypeList.POPULAR_MOVIES
            true
        }
        R.id.sort_by_votes -> {
            viewModel.setListType(MainViewModel.MovieTypeList.TOP_RATED_MOVIES)
            //viewModel.listType = MainViewModel.MovieTypeList.TOP_RATED_MOVIES
            true
        }
        R.id.show_favourites -> {
            viewModel.setListType(MainViewModel.MovieTypeList.FAVOURITE_MOVIES)
            //viewModel.listType = MainViewModel.MovieTypeList.FAVOURITE_MOVIES
            true
        }
        else -> {
            false
        }

    }



    override fun onClick(movie: Movie?) {
        findNavController().navigate(R.id.action_mainViewFragment_to_detailInformationFragment)
    }
}