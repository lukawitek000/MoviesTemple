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
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.android.popularmovies.models.Movie
import com.example.android.popularmovies.utilities.NetworkUtil
import com.example.android.popularmovies.utilities.MovieJsonConvert
import org.json.JSONException
import kotlin.Throws
import com.example.android.popularmovies.models.Review
import java.io.IOException

class MainActivity : AppCompatActivity(), MovieAdapterOnClickHandler {
    private var movieAdapter: MoviesAdapter? = null
    private var selectedSorting: String? = null
    private lateinit var failureTextView: TextView
    private lateinit var progressBar: ProgressBar

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViewModel()

        setUpRecyclerView()
        progressBar = findViewById<ProgressBar>(R.id.progress_bar)
        progressBar.visibility = View.INVISIBLE
        failureTextView = findViewById(R.id.failure_text_view)
        failureTextView.visibility = View.INVISIBLE

        /*
        if (savedInstanceState != null) {
            selectedSorting = savedInstanceState.getString(SELECTED_SORTING)
            assert(selectedSorting != null)
            when (selectedSorting) {
                FAVOURITES -> setTitle(R.string.favourites_title)
                POPULAR -> setTitle(R.string.popular_movie_title)
                else -> setTitle(R.string.vote_average_label)
            }
        } else {
            selectedSorting = POPULAR
            setTitle(R.string.popular_movie_title)
        }
        if (selectedSorting == FAVOURITES) {
            setupViewModel()
        } else {
            updateUI()
        }*/
    }

    private fun setUpRecyclerView() {
        val spanCount = calculateSpanCount()
        val movieRecyclerView = findViewById<RecyclerView>(R.id.recyclerview_movies)
        val layoutManager = GridLayoutManager(this, spanCount, LinearLayoutManager.VERTICAL, false)
        movieRecyclerView.layoutManager = layoutManager
        movieRecyclerView.setHasFixedSize(true)
        movieAdapter = MoviesAdapter(this)
        movieRecyclerView.adapter = movieAdapter
    }

    private fun calculateSpanCount(): Int {
        val point = Point()
        val display = windowManager.defaultDisplay
        display.getSize(point)
        val display_width = point.x
        return Math.round(display_width.toFloat() / IMAGE_WIDTH)
    }

    private fun setupViewModel() {
        val viewModelFactory = MainViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
    }

    private fun updateUI() {
    //    FetchMovies().execute()
    }

    override fun onClick(movie: Movie?) {
        val intent = Intent(this, DetailInformation::class.java)
        intent.putExtra(MOVIE_KEY, movie!!.id)
        intent.putExtra(TITLE_KEY, title)
        startActivity(intent)
    }

    private fun getMovieFromMovieEntity(movieEntity: MovieEntity): Movie {
        val movie = Movie()
        movie.id = movieEntity.id
        movie.id = movieEntity.id
        movie.originalTitle = movieEntity.originalTitle!!
        movie.title = movieEntity.title!!
        movie.poster = movieEntity.posterUri
        movie.overview = movieEntity.overview!!
        movie.voteAverage = movieEntity.voteAverage
        movie.releaseDate = movieEntity.releaseDate!!
        movie.videoUrls = movieEntity.videoUrls
        movie.reviews = movieEntity.reviews
        movie.isFavourite = true
        return movie
    }
/*
    @SuppressLint("StaticFieldLeak")
    internal inner class FetchMovies : AsyncTask<Void?, Void?, Array<Movie>?>() {
        override fun onPreExecute() {
            progressBar!!.visibility = View.VISIBLE
            failureTextView!!.visibility = View.INVISIBLE
        }

        protected override fun doInBackground(vararg voids: Void): Array<Movie>? {
            if (selectedSorting != FAVOURITES) {
                val movieUrl = NetworkUtil.buildUrl(selectedSorting)
                try {
                    val answer = NetworkUtil.getResponseFromHttpUrl(movieUrl)
                    val fetchedMovies = MovieJsonConvert.getMovieFromJson(answer)
                    getVideosFromApi(fetchedMovies)
                    getReviewsFromApi(fetchedMovies)
                    return fetchedMovies
                } catch (e: IOException) {
                    e.printStackTrace()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
            return null
        }

        @Throws(IOException::class, JSONException::class)
        private fun getReviewsFromApi(fetchedMovies: Array<Movie>) {
            for (movie in fetchedMovies) {
                val movieUrl = NetworkUtil.buildUrl(movie.id.toString() + "/reviews")
                val answer = NetworkUtil.getResponseFromHttpUrl(movieUrl)
                val review = MovieJsonConvert.getReviewsFromJson(answer)
                movie.reviews = review
            }
        }

        @Throws(IOException::class, JSONException::class)
        private fun getVideosFromApi(fetchedMovies: Array<Movie>) {
            for (movie in fetchedMovies) {
                val movieUrl = NetworkUtil.buildUrl(movie.id.toString() + "/videos")
                val answer = NetworkUtil.getResponseFromHttpUrl(movieUrl)
                movie.videoUrls = MovieJsonConvert.getVideoUrlFromJson(answer)
            }
        }

        override fun onPostExecute(movies: Array<Movie>?) {
            progressBar!!.visibility = View.INVISIBLE
            if (movies != null) {
                movieAdapter!!.setMoviesData(movies)
            } else {
                failureTextView!!.visibility = View.VISIBLE
            }
        }
    }*/

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.sort_by_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        when (selectedSorting) {
            POPULAR -> {
                menu.findItem(R.id.sort_by_popularity).isVisible = false
                menu.findItem(R.id.sort_by_votes).isVisible = true
                menu.findItem(R.id.show_favourites).isVisible = true
            }
            TOP_RATED -> {
                menu.findItem(R.id.sort_by_votes).isVisible = false
                menu.findItem(R.id.sort_by_popularity).isVisible = true
                menu.findItem(R.id.show_favourites).isVisible = true
            }
            FAVOURITES -> {
                menu.findItem(R.id.sort_by_votes).isVisible = true
                menu.findItem(R.id.sort_by_popularity).isVisible = true
                menu.findItem(R.id.show_favourites).isVisible = false
            }
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.sort_by_popularity) {
            selectedSorting = POPULAR
            setTitle(R.string.popular_movie_title)
            updateUI()
            return true
        } else if (id == R.id.sort_by_votes) {
            selectedSorting = TOP_RATED
            setTitle(R.string.top_rated_title)
            updateUI()
            return true
        } else if (id == R.id.show_favourites) {
            selectedSorting = FAVOURITES
            setupViewModel()
            setTitle(R.string.favourites_title)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(SELECTED_SORTING, selectedSorting)
        super.onSaveInstanceState(outState)
    }

    companion object {
        private const val SELECTED_SORTING = "SELECTED_SORTING"
        const val MOVIE_KEY = "MOVIE"
        const val TITLE_KEY = "TITLE"
        private const val FAVOURITES = "favourites"
        private const val TOP_RATED = "top_rated"
        private const val POPULAR = "popular"
        private const val IMAGE_WIDTH = 342
    }
}