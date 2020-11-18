package com.example.android.popularmovies.adapters

import android.content.Context
import android.graphics.Point
import android.net.Uri
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.android.popularmovies.adapters.MoviesAdapter.MovieAdapterOnClickHandler
import androidx.recyclerview.widget.RecyclerView
import com.example.android.popularmovies.adapters.MoviesAdapter.MoviesAdapterViewHolder
import com.example.android.popularmovies.R
import com.example.android.popularmovies.adapters.MoviesAdapter
import com.squareup.picasso.Picasso
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.android.popularmovies.models.Movie

class MoviesAdapter(private val clickHandler: MovieAdapterOnClickHandler) : ListAdapter<Movie, MoviesAdapterViewHolder>(MoviesDiffCallback()) {

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
        val movie = getItem(position)
        Picasso.with(holder.posterImage.context)
                .load(movie.posterUri)
                .into(holder.posterImage)
    }


    inner class MoviesAdapterViewHolder(itemView: View) : ViewHolder(itemView), View.OnClickListener {
        val posterImage: ImageView = itemView.findViewById(R.id.poster_image_view)
        override fun onClick(view: View) {
            val adapterPosition = adapterPosition
            clickHandler.onClick(getItem(adapterPosition))
        }


        init {
            /*val windowManager = itemView.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display: Display
            display = windowManager.defaultDisplay
            val point = Point()
            display.getSize(point)
            val displayWidth = point.x
            posterImage.minimumWidth = displayWidth*/
            itemView.setOnClickListener(this)
        }
    }
}


class MoviesDiffCallback : DiffUtil.ItemCallback<Movie>(){
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem == newItem
    }
}