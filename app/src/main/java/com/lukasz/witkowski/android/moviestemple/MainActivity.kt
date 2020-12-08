package com.lukasz.witkowski.android.moviestemple


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.lukasz.witkowski.android.moviestemple.databinding.ActivityMainBinding
import com.lukasz.witkowski.android.moviestemple.fragments.DetailInformationFragment

class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {


    private lateinit var binding: ActivityMainBinding
    private lateinit var  navHostFragment: NavHostFragment
    private val appBarConfiguration = AppBarConfiguration(
            setOf(
                    R.id.popularMoviesFragment,
                    R.id.topRatedMoviesFragment,
                    R.id.favouriteMoviesFragment,
                    R.id.recommendMoviesFragment
            )
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigation.setupWithNavController(navController)

        setSupportActionBar(binding.toolbar)
        navController.addOnDestinationChangedListener(this)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }


    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.fragment_container).navigateUp() || super.onSupportNavigateUp()
    }


    fun setBottomNavigationVisibility(value: Int, isAnimated: Boolean){
        val collapseListener = object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {}

            override fun onAnimationEnd(p0: Animation?) {
                binding.bottomNavigation.visibility = value
            }

            override fun onAnimationRepeat(p0: Animation?) {}

        }
        val anim: TranslateAnimation
        if(value == View.GONE){
            anim = TranslateAnimation(0.0f, -binding.bottomNavigation.width.toFloat(), 0.0f, 0.0f)
        }else{
            binding.bottomNavigation.visibility = value
            anim = TranslateAnimation(-binding.bottomNavigation.width.toFloat(), 0.0f , 0.0f, 0.0f)
        }
        anim.setAnimationListener(collapseListener)
        if(isAnimated) {
            anim.duration = resources.getInteger(R.integer.slide_animation_time).toLong()
        }else{
            anim.duration = 0
        }
        binding.bottomNavigation.startAnimation(anim)
    }


    fun changeToolbarTitle(title: String){
        binding.toolbar.title = title
        binding.toolbar.navigationIcon = null
    }


    override fun onDestinationChanged(controller: NavController, destination: NavDestination, arguments: Bundle?) {
        if(destination.label == resources.getString(R.string.detail_information_label)){
            animateOutToolbar()
        }else{
            if(!navHostFragment.childFragmentManager.fragments.isNullOrEmpty()) {
                val currentFragment = navHostFragment.childFragmentManager.fragments[0]
                if(currentFragment.javaClass.simpleName == DetailInformationFragment.TAG){
                    binding.toolbar.setupWithNavController(controller, appBarConfiguration)
                    setSupportActionBar(binding.toolbar)
                    animateInToolbar()
                }
            }
        }
    }


    private fun animateInToolbar() {
        val animation = TranslateAnimation(-binding.toolbar.width.toFloat(), 0f, 0f, 0f)
        animation.duration = resources.getInteger(R.integer.slide_animation_time).toLong()
        binding.toolbar.startAnimation(animation)
    }


    private fun animateOutToolbar() {
        val animation = TranslateAnimation(0f, -binding.toolbar.width.toFloat(), 0f, 0f)
        animation.duration = resources.getInteger(R.integer.slide_animation_time).toLong()
        binding.toolbar.startAnimation(animation)
    }
}