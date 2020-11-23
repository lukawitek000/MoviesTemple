package com.lukasz.witkowski.android.moviestemple


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.lukasz.witkowski.android.moviestemple.utilities.IMAGE_WIDTH
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {


    lateinit var bottomNavigation: BottomNavigationView

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
        val viewModelFactory = MainViewModelFactory(application)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        viewModel.databaseValues.observe(this, Observer {
            if( it!= null){
                viewModel.setResponseFromDatabaseToFavouriteMovies(it)
            }
        }
        )
        
        Log.i("MainActivity", "bottom nav height ${bottomNavigation.height} ")
        
    }


    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.fragment_container).navigateUp() || super.onSupportNavigateUp()
    }

    fun setBottomNavigationVisibility(value: Int, isAnimated: Boolean){
        val collapseListener = object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {
            }

            override fun onAnimationEnd(p0: Animation?) {
                bottomNavigation.visibility = value
            }

            override fun onAnimationRepeat(p0: Animation?) {
            }

        }
        val anim: TranslateAnimation
        if(value == View.GONE){
            anim = TranslateAnimation(0.0f, -bottomNavigation.width.toFloat(), 0.0f, 0.0f)
        }else{
            bottomNavigation.visibility = value
           anim = TranslateAnimation(-bottomNavigation.width.toFloat(), 0.0f , 0.0f, 0.0f)

        }
        anim.setAnimationListener(collapseListener)
        if(isAnimated) {
            anim.duration = 300
        }else{
            anim.duration = 0
        }
        bottomNavigation.startAnimation(anim)
    }

    fun calculateSpanCount(): Int {
        val displayWidth = resources.displayMetrics.widthPixels
        Log.i("PopularMoviesFragment", "display width : $displayWidth")
        return displayWidth/ IMAGE_WIDTH + 1
    }
}