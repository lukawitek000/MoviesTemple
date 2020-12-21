package com.lukasz.witkowski.android.moviestemple.adapters


import android.view.*
import android.widget.ImageView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.lukasz.witkowski.android.moviestemple.R
import com.lukasz.witkowski.android.moviestemple.adapters.MoviesAdapter.MoviesAdapterViewHolder
import com.lukasz.witkowski.android.moviestemple.models.Movie


class MoviesAdapter(private val clickHandler: MovieAdapterOnClickHandler)
    : PagingDataAdapter<Movie, MoviesAdapterViewHolder>(MoviesDiffCallback()) {

    companion object {
        const val MOVIE_POSTER = 1
        const val LOADING_FOOTER = 0
    }

    interface MovieAdapterOnClickHandler {
        fun onMovieClick(movie: Movie)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesAdapterViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.movies_list_item, parent, false)
        return MoviesAdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: MoviesAdapterViewHolder, position: Int) {
        val movie = getItem(position)
        if(movie != null){
            holder.bind(movie)
        }

    }

    override fun getItemViewType(position: Int): Int{
        return if(position == itemCount){
            MOVIE_POSTER
        }else{
            LOADING_FOOTER
        }
    }


    inner class MoviesAdapterViewHolder(itemView: View) : ViewHolder(itemView), View.OnClickListener {
        private val posterImage: ImageView = itemView.findViewById(R.id.iv_movie_poster)
        private var movie: Movie? = null

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            clickHandler.onMovieClick(movie!!)
        }

        fun bind(movie: Movie?){
            if (movie != null){
                this.movie = movie
                if(movie.posterUri != null) {
                    Glide.with(itemView.context)
                            .load(movie.posterUri)
                            .placeholder(R.drawable.poster_placeholder)
                            .into(posterImage)
                }else{
                    Glide.with(itemView.context)
                            .load(R.drawable.default_movie_poster)
                            .placeholder(R.drawable.poster_placeholder)
                            .into(posterImage)
                }
            }
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