package com.example.android.popularmovies.adapters

import android.content.Context
import com.example.android.popularmovies.adapters.TrailersAdapter.TrailerClickListener
import androidx.recyclerview.widget.RecyclerView
import com.example.android.popularmovies.adapters.TrailersAdapter.TrailersAdapterTrailerHolder
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.example.android.popularmovies.R
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import android.widget.TextView
import com.example.android.popularmovies.models.Video

class TrailersAdapter(private val trailers: List<Video>?, private val trailerClickListener: TrailerClickListener) : RecyclerView.Adapter<TrailersAdapterTrailerHolder>() {

    interface TrailerClickListener {
        fun onTrailerClicked(trailer: Video)
    }

    private var context: Context? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrailersAdapterTrailerHolder {
        context = parent.context
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.trailers_list_item, parent, false)
        return TrailersAdapterTrailerHolder(view)
    }

    override fun onBindViewHolder(holder: TrailersAdapterTrailerHolder, position: Int) {
        val text = context!!.getString(R.string.trailer_value, position + 1)
        holder.trailerText.text = text
    }

    override fun getItemCount(): Int {
        return trailers?.size ?: 0
    }

    inner class TrailersAdapterTrailerHolder(itemView: View) : ViewHolder(itemView), View.OnClickListener {
        val trailerText: TextView = itemView.findViewById(R.id.trailer_label)

        override fun onClick(v: View) {
            val adapterPosition = adapterPosition
            trailerClickListener.onTrailerClicked(trailers!![adapterPosition])
        }

        init {
            itemView.setOnClickListener(this)
        }
    }
}