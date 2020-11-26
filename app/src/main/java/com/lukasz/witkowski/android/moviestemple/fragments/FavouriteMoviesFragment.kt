package com.lukasz.witkowski.android.moviestemple.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import androidx.fragment.app.Fragment
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingData
import androidx.paging.map
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
import com.lukasz.witkowski.android.moviestemple.models.toMovie
import com.lukasz.witkowski.android.moviestemple.models.toReview
import com.lukasz.witkowski.android.moviestemple.models.toVideo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class FavouriteMoviesFragment : BaseListMoviesFragment(), MoviesAdapter.MovieAdapterOnClickHandler{

    //private lateinit var moviesAdapter: MoviesAdapter
    //private lateinit var recyclerView: RecyclerView
   // private val shareViewModel by activityViewModels<MainViewModel> { MainViewModelFactory(requireActivity().application) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.movies_poster_list_layout, container, false)

        moviesAdapter = MoviesAdapter(this)
        setUpRecyclerView()

        setUpObservers()

        refreshOnSwipe()
        initAdapter()
       // (requireActivity() as MainActivity).setVisibilityBaseOnStatus(MainViewModel.Status.SUCCESS, "")
        getFavouriteMovies()
        //moviesAdapter.refresh()
        setHasOptionsMenu(true)
        return binding.root
    }

    private var job: Job? = null

    private fun getFavouriteMovies() {
        job?.cancel()
        job = lifecycleScope.launch {
            @OptIn(ExperimentalCoroutinesApi::class)
            sharedViewModel.favouriteMovies.collectLatest { pagingData ->

                val movies: PagingData<Movie> = pagingData.mapSync { pagingData ->
                    Movie(pagingData.posterPath, pagingData.id, pagingData.originalTitle, pagingData.title,
                            pagingData.voteAverage, pagingData.overview, pagingData.releaseDate)
                   /* Movie(pagingData.movie.posterPath, pagingData.movie.id, pagingData.movie.originalTitle, pagingData.movie.title,
                            pagingData.movie.voteAverage, pagingData.movie.overview, pagingData.movie.releaseDate,
                            emptyList(), pagingData.videos.map { it.toVideo()  }, pagingData.reviews.map { it.toReview() })*/
                }

                moviesAdapter.submitData(movies)
            }
        }
    }


    /* private fun setUpRecyclerView() {
         val spanCount = (activity as MainActivity).calculateSpanCount()
         val layoutManager = GridLayoutManager(requireContext(), spanCount, LinearLayoutManager.VERTICAL, false)
         recyclerView.layoutManager = layoutManager
         recyclerView.setHasFixedSize(true)
         moviesAdapter = MoviesAdapter(this)
         recyclerView.adapter = moviesAdapter
     }*/



    private fun setUpObservers() {
        /*sharedViewModel.databaseValues.observe(viewLifecycleOwner, Observer {
            Log.i("FavouriteMoviesFragment", "database value $it")
            if(it != null){
               // moviesAdapter.submitList(it)
                if(it.isEmpty()){
                    (requireActivity() as MainActivity).setVisibilityBaseOnStatus(
                            MainViewModel.Status.FAILURE,
                            "You don't have favourite movies yet")
                }else{
                    (requireActivity() as MainActivity).setVisibilityBaseOnStatus(
                            MainViewModel.Status.SUCCESS, "")
                }
            }
        })*/
    }

    override fun onClick(movie: Movie) {
        sharedViewModel.selectMovie(movie)
        findNavController().navigate(R.id.action_favouriteMoviesFragment_to_detailInformationFragment)
        (activity as MainActivity).changeToolbarTitle(resources.getString(R.string.favourites_title))
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        return inflater.inflate(R.menu.favourite_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId){
        R.id.delete_all_item -> {
            createAlertDialog().show()
            true
        }
        else -> false
    }

    private fun createAlertDialog(): AlertDialog {
        val builder = AlertDialog.Builder(requireContext())
        val view = layoutInflater.inflate(R.layout.delete_all_custom_dialog, null)

        builder.setView(view)
        val dialog = builder.create()
        view.findViewById<Button>(R.id.delete_all_yes_button).setOnClickListener {
            sharedViewModel.deleteAllFavouriteMovies()
            Toast.makeText(requireContext(), "All favourite movies deleted", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        view.findViewById<Button>(R.id.delete_all_cancel_button).setOnClickListener {
            dialog.dismiss()
        }
        return dialog

    }
}