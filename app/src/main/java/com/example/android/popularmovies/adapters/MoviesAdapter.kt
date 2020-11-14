package com.example.android.popularmovies.adapters

import android.content.Context
import android.graphics.Point
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import com.example.android.popularmovies.adapters.MoviesAdapter.MovieAdapterOnClickHandler
import androidx.recyclerview.widget.RecyclerView
import com.example.android.popularmovies.adapters.MoviesAdapter.MoviesAdapterViewHolder
import com.example.android.popularmovies.R
import com.example.android.popularmovies.adapters.MoviesAdapter
import com.squareup.picasso.Picasso
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.android.popularmovies.models.Movie

class MoviesAdapter(private val clickHandler: MovieAdapterOnClickHandler) : RecyclerView.Adapter<MoviesAdapterViewHolder>() {

    interface MovieAdapterOnClickHandler {
        fun onClick(movie: Movie?)
    }

    private var moviesData: List<Movie>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesAdapterViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.movies_list_item, parent, false)
        return MoviesAdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: MoviesAdapterViewHolder, position: Int) {
        val movie = moviesData!![position]
        holder.response.text = movie.toString()
       // Picasso.with(holder.posterImage.context)
        //        .load(movie.poster)
        //        .into(holder.posterImage)
    }

    override fun getItemCount(): Int {
        return moviesData?.size ?: 0
    }

    fun setMoviesData(movies: List<Movie>) {
        moviesData = movies
        notifyDataSetChanged()
    }

    inner class MoviesAdapterViewHolder(itemView: View) : ViewHolder(itemView), View.OnClickListener {
       // val posterImage: ImageView = itemView.findViewById(R.id.poster_image_view)
        override fun onClick(view: View) {
            val adapterPosition = adapterPosition
            clickHandler.onClick(moviesData!![adapterPosition])
        }

        val response: TextView = itemView.findViewById(R.id.response)
/*
        init {
            val windowManager = itemView.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display: Display
            display = windowManager.defaultDisplay
            val point = Point()
            display.getSize(point)
            val displayWidth = point.x
            posterImage.minimumWidth = displayWidth
            itemView.setOnClickListener(this)
        }*/
    }
}