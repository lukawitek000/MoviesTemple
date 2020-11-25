package com.lukasz.witkowski.android.moviestemple.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.lukasz.witkowski.android.moviestemple.MainActivity
import com.lukasz.witkowski.android.moviestemple.R
import com.lukasz.witkowski.android.moviestemple.adapters.MoviesAdapter
import com.lukasz.witkowski.android.moviestemple.models.Movie
import com.lukasz.witkowski.android.moviestemple.viewModels.TopRatedMoviesViewModel
import com.lukasz.witkowski.android.moviestemple.viewModels.TopRatedMoviesViewModelFactory
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class TopRatedMoviesFragment : BaseListMoviesFragment(), MoviesAdapter.MovieAdapterOnClickHandler {

   // private lateinit var moviesAdapter: MoviesAdapter

   // private lateinit var moviesRecyclerView: RecyclerView

   // private val sharedViewModel by activityViewModels<MainViewModel> { MainViewModelFactory(requireActivity().application) }
    private val viewModel by viewModels<TopRatedMoviesViewModel> { TopRatedMoviesViewModelFactory(requireActivity().application) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.movies_poster_list_layout, container, false)
        moviesRecyclerView = view.findViewById(R.id.movies_recyclerview)

       // viewModel.getTopRatedMovies()
        moviesAdapter = MoviesAdapter(this)
        setUpRecyclerView()
       // setUpObservers()

        val refresh = view.findViewById<SwipeRefreshLayout>(R.id.swipe_refresh_layout)
        refreshOnSwipe(refresh)
       /* refresh.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(requireContext(), R.color.darkYellow))
        refresh.setColorSchemeColors(Color.BLACK)

        refresh.setOnRefreshListener {
            Toast.makeText(requireContext(), "Refreshed", Toast.LENGTH_SHORT).show()
            viewModel.getTopRatedMovies()
            refresh.isRefreshing = false
        }*/

        initAdapter()
        getTopRatedMovies()


        return view
    }

    private var job: Job? = null

    private fun getTopRatedMovies(){
        job?.cancel()
        job = lifecycleScope.launch {
            viewModel.getTopRatedMovies().collectLatest{
                moviesAdapter.submitData(it)
            }
        }
    }


  /*  private fun setUpRecyclerView() {
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
               // moviesAdapter.submitList(it)
            }
        })

        viewModel.topRatedMoviesStatus.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                (requireActivity() as MainActivity).setVisibilityBaseOnStatus(
                        it,
                        "Cannot connect to server, check your favourite movies")
            }
        })
    }*/


    override fun onClick(movie: Movie) {
        sharedViewModel.selectMovie(movie)
        findNavController().navigate(R.id.action_topRatedMoviesFragment_to_detailInformationFragment)
        (activity as MainActivity).changeToolbarTitle(resources.getString(R.string.top_rated_title))
    }


}