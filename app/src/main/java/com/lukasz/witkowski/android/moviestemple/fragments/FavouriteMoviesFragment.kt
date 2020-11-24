package com.lukasz.witkowski.android.moviestemple.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import androidx.fragment.app.Fragment
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lukasz.witkowski.android.moviestemple.MainActivity
import com.lukasz.witkowski.android.moviestemple.MainViewModel
import com.lukasz.witkowski.android.moviestemple.MainViewModelFactory
import com.lukasz.witkowski.android.moviestemple.R
import com.lukasz.witkowski.android.moviestemple.adapters.MoviesAdapter
import com.lukasz.witkowski.android.moviestemple.models.Movie


class FavouriteMoviesFragment : Fragment(), MoviesAdapter.MovieAdapterOnClickHandler{

    private lateinit var moviesAdapter: MoviesAdapter


    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyFavouriteMoviesListTextView: TextView

    private val shareViewModel by activityViewModels<MainViewModel> { MainViewModelFactory(requireActivity().application) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        val view = inflater.inflate(R.layout.fragment_favourite_movies, container, false)
        recyclerView = view.findViewById(R.id.favourite_movies_recyclerview)
        emptyFavouriteMoviesListTextView = view.findViewById(R.id.empty_list_favourite_movies_textview)
        setUpRecyclerView()
        setUpObservers()
        setHasOptionsMenu(true)
        return view
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
        shareViewModel.databaseValues.observe(viewLifecycleOwner, Observer {
            Log.i("FavouriteMoviesFragment", "database value $it")
            if(it != null){
                moviesAdapter.submitList(it)
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
        shareViewModel.selectMovie(movie)
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
            shareViewModel.deleteAllFavouriteMovies()
            Toast.makeText(requireContext(), "All favourite movies deleted", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        view.findViewById<Button>(R.id.delete_all_cancel_button).setOnClickListener {
            dialog.dismiss()
        }
        return dialog

    }
}