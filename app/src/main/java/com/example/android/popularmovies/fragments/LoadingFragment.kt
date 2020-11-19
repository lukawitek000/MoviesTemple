package com.example.android.popularmovies.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.popularmovies.MainViewModel
import com.example.android.popularmovies.MainViewModelFactory
import com.example.android.popularmovies.R


class LoadingFragment : Fragment() {

    private lateinit var viewModel: MainViewModel

    companion object{
        private val TAG = LoadingFragment::class.simpleName
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        setUpViewModel()
        setUpObservers()

        return inflater.inflate(R.layout.fragment_loading, container, false)
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
                    }
                    MainViewModel.Status.LOADING -> {
                        Log.i(TAG, "loading")
                    }
                    else -> {
                        Log.i(TAG, "setUpObservers: errror")
                    }

                }
            }
        })
    }

}