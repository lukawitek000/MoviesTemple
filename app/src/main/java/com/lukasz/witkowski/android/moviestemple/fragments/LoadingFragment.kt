package com.lukasz.witkowski.android.moviestemple.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.lukasz.witkowski.android.moviestemple.MainViewModel
import com.lukasz.witkowski.android.moviestemple.MainViewModelFactory
import com.lukasz.witkowski.android.moviestemple.R


class LoadingFragment : Fragment() {

    private lateinit var viewModel: MainViewModel

    companion object{
        private val TAG = LoadingFragment::class.simpleName
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        //(requireActivity() as MainActivity).setBottomNavigationVisibility(View.GONE, true)
        val view = inflater.inflate(R.layout.fragment_loading, container, false)
        setUpViewModel()
        setUpObservers()
        //viewModel.getMovies()
        return view
    }



    private fun setUpViewModel() {
        val viewModelFactory = MainViewModelFactory(requireActivity().application)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(MainViewModel::class.java)
    }

    private fun setUpObservers() {
        viewModel.popularMoviesStatus.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                when (it) {
                    MainViewModel.Status.SUCCESS -> {
                        findNavController().navigate(R.id.action_loadingFragment_to_popularMoviesFragment)
                        //(requireActivity() as MainActivity).setBottomNavigationVisibility(View.VISIBLE, true)
                    }
                    MainViewModel.Status.LOADING -> {
                        Log.i(TAG, "loading")
                    }
                    else -> {
                        findNavController().navigate(R.id.action_loadingFragment_to_favouriteMoviesFragment)
                       // (requireActivity() as MainActivity).setBottomNavigationVisibility(View.VISIBLE, true)
                        Log.i(TAG, "setUpObservers: errror")
                    }

                }
            }
        })
    }

}