package com.example.android.popularmovies


import androidx.appcompat.app.AppCompatActivity
import com.example.android.popularmovies.adapters.MoviesAdapter.MovieAdapterOnClickHandler
import com.example.android.popularmovies.adapters.MoviesAdapter
import android.widget.TextView
import android.widget.ProgressBar
import android.os.Bundle
import com.example.android.popularmovies.R
import com.example.android.popularmovies.MainActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.Display
import com.example.android.popularmovies.MainViewModel
import androidx.lifecycle.ViewModelProviders
import com.example.android.popularmovies.database.MovieEntity
import android.content.Intent
import com.example.android.popularmovies.DetailInformation
import android.annotation.SuppressLint
import android.graphics.Point
import android.os.AsyncTask
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.android.popularmovies.models.Movie

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, MainViewFragment.newInstance())
                .commit()
    }
}