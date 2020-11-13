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

class TrailersAdapter(private val trailerClickListener: TrailerClickListener) : RecyclerView.Adapter<TrailersAdapterTrailerHolder>() {
    private var trailers: Array<String>?

    interface TrailerClickListener {
        fun onTrailerClicked(trailer: String?)
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
        return if (trailers != null) {
            trailers!!.size
        } else 0
    }

    fun setTrailers(trailers: Array<String>?) {
        this.trailers = trailers
    }

    inner class TrailersAdapterTrailerHolder(itemView: View) : ViewHolder(itemView), View.OnClickListener {
        val trailerText: TextView
        override fun onClick(v: View) {
            val adapterPosition = adapterPosition
            trailerClickListener.onTrailerClicked(trailers!![adapterPosition])
        }

        init {
            trailerText = itemView.findViewById(R.id.trailer_label)
            itemView.setOnClickListener(this)
        }
    }
}