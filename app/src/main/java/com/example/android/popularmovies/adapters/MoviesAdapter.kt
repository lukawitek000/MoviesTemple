package com.example.android.popularmovies.adapters

import android.content.Context
import android.graphics.Point
import android.view.*
import android.widget.ImageView
import com.example.android.popularmovies.models.Movie.poster
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesAdapterViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.movies_list_item, parent, false)
        return MoviesAdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: MoviesAdapterViewHolder, position: Int) {
        val (_, _, _, poster) = moviesData!![position]
        Picasso.with(holder.posterImage.context)
                .load(poster)
                .into(holder.posterImage)
    }

    override fun getItemCount(): Int {
        return if (moviesData != null) {
            moviesData!!.size
        } else 0
    }

    fun setMoviesData(movies: Array<Movie>?) {
        moviesData = movies
        notifyDataSetChanged()
    }

    inner class MoviesAdapterViewHolder(itemView: View) : ViewHolder(itemView), View.OnClickListener {
        val posterImage: ImageView
        override fun onClick(view: View) {
            val adapterPosition = adapterPosition
            clickHandler.onClick(moviesData!![adapterPosition])
        }

        init {
            posterImage = itemView.findViewById(R.id.poster_image_view)
            val windowManager = itemView.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display: Display
            if (windowManager != null) {
                display = windowManager.defaultDisplay
                val point = Point()
                display.getSize(point)
                val display_width = point.x
                posterImage.minimumWidth = display_width
            }
            itemView.setOnClickListener(this)
        }
    }

    companion object {
        var moviesData: Array<Movie>?
            private set
    }
}