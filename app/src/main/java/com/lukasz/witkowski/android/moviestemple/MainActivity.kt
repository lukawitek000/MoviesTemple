package com.lukasz.witkowski.android.moviestemple


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.Toolbar
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.lukasz.witkowski.android.moviestemple.utilities.IMAGE_WIDTH
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.lukasz.witkowski.android.moviestemple.databinding.ActivityMainBinding
import com.lukasz.witkowski.android.moviestemple.fragments.DetailInformationFragment

class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {


    private lateinit var binding: ActivityMainBinding

    private lateinit var bottomNavigation: BottomNavigationView

    private lateinit var toolbar: androidx.appcompat.widget.Toolbar

    private val appBarConfiguration = AppBarConfiguration(
            setOf(
                    R.id.popularMoviesFragment,
                    R.id.topRatedMoviesFragment,
                    R.id.favouriteMoviesFragment,
                    R.id.recommendMoviesFragment
            )
    )

    private lateinit var  navHostFragment: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        val navController = navHostFragment.navController
       // bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation = binding.bottomNavigation
        bottomNavigation.setupWithNavController(navController)

        toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        navController.addOnDestinationChangedListener(this)

        setupActionBarWithNavController(navController, appBarConfiguration)

        val viewModelFactory = MainViewModelFactory(application)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        viewModel.databaseValues.observe(this, Observer {
            if( it!= null){
               // viewModel.setResponseFromDatabaseToFavouriteMovies(it)
            }
        }
        )

    }

    fun setVisibilityBaseOnStatus(status: MainViewModel.Status, failureMessage: String) {
        Log.i("MainActivity", "set visibility status = ${status.name}")
        binding.fragmentContainer.visibility = View.VISIBLE
        when (status) {
            MainViewModel.Status.SUCCESS -> {
                binding.progressbar.visibility = View.GONE
                //binding.fragmentContainer.visibility = View.VISIBLE
                //animateInFragment()
                binding.errorMessageTextview.visibility = View.GONE
            }
            MainViewModel.Status.LOADING -> {
                binding.progressbar.visibility = View.VISIBLE
               // binding.fragmentContainer.visibility = View.GONE
                binding.errorMessageTextview.visibility = View.GONE
            }
            else -> {
                binding.errorMessageTextview.text = failureMessage
                binding.progressbar.visibility = View.GONE
               // binding.fragmentContainer.visibility = View.GONE
                binding.errorMessageTextview.visibility = View.VISIBLE
            }

        }
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
            anim.duration = resources.getInteger(R.integer.slide_animation_time).toLong()
        }else{
            anim.duration = 0
        }
        bottomNavigation.startAnimation(anim)
    }

    fun calculateSpanCount(): Int {
        val displayWidth = resources.displayMetrics.widthPixels
        val dp = displayWidth / resources.displayMetrics.density;
        Log.i("PopularMoviesFragment", "display width : $displayWidth dp: $dp")
        return displayWidth/ IMAGE_WIDTH + 1
    }

    fun changeToolbarTitle(title: String){
        toolbar.title = title
        toolbar.navigationIcon = null
    }


    override fun onDestinationChanged(controller: NavController, destination: NavDestination, arguments: Bundle?) {

        //Log.i("MainActivity", "onDestinationChanged dest ${destination.label}")
        if(destination.label == "Detail Information"){
            animateOutToolbar()
        }else{
            if(!navHostFragment.childFragmentManager.fragments.isNullOrEmpty()) {
                val currentFragment = navHostFragment.childFragmentManager.fragments[0]
                //Log.i("MainActivity", "onDestinationChanged current fragment ${currentFragment.javaClass.simpleName}")
                if(currentFragment.javaClass.simpleName == "DetailInformationFragment"){
                   // Log.i("MainActivity", "onDestinationChange set up with nav controller")
                    toolbar.setupWithNavController(controller, appBarConfiguration)
                    setSupportActionBar(toolbar)
                    animateInToolbar()
                }
            }
        }
    }

    private fun animateInToolbar() {
        val animation = TranslateAnimation(-toolbar.width.toFloat(), 0f, 0f, 0f)
        animation.duration = resources.getInteger(R.integer.slide_animation_time).toLong()
        toolbar.startAnimation(animation)
    }

    private fun animateOutToolbar() {
        val animation = TranslateAnimation(0f, -toolbar.width.toFloat(), 0f, 0f)
        animation.duration = resources.getInteger(R.integer.slide_animation_time).toLong()
        toolbar.startAnimation(animation)
    }

}