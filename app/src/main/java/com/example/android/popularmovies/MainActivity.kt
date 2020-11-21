package com.example.android.popularmovies


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.android.popularmovies.utilities.IMAGE_WIDTH
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {


    private lateinit var bottomNavigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        val navController = navHostFragment.navController
        bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation.setupWithNavController(navController)
        val appBarConfiguration = AppBarConfiguration(
                setOf(
                        R.id.loadingFragment,
                        R.id.popularMoviesFragment,
                        R.id.topRatedMoviesFragment,
                        R.id.favouriteMoviesFragment,
                        R.id.recommendMoviesFragment
                )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

    }

    override fun onSupportNavigateUp(): Boolean {
        bottomNavigation.visibility = View.VISIBLE
        return findNavController(R.id.fragment_container).navigateUp() || super.onSupportNavigateUp()
    }

    fun setBottomNavigationVisibility(value: Int){
        bottomNavigation.visibility = value
    }

    fun calculateSpanCount(): Int {
        val displayWidth = resources.displayMetrics.widthPixels
        Log.i("PopularMoviesFragment", "display width : $displayWidth")
        return displayWidth/ IMAGE_WIDTH + 1
    }
}