package com.lukasz.witkowski.android.moviestemple.adapters

import androidx.recyclerview.widget.RecyclerView
import com.lukasz.witkowski.android.moviestemple.adapters.ReviewsAdapter.ReviewsAdapterViewHolder
import com.lukasz.witkowski.android.moviestemple.models.Review
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.lukasz.witkowski.android.moviestemple.R
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import android.widget.TextView

class ReviewsAdapter() : RecyclerView.Adapter<ReviewsAdapterViewHolder>() {

    var reviews: List<Review> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewsAdapterViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.reviews_list_item, parent, false)
        return ReviewsAdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewsAdapterViewHolder, position: Int) {
        val review = reviews[position]
        holder.content.text = review.content
        holder.author.text = review.author
    }

    override fun getItemCount(): Int {
        return reviews.size
    }


    class ReviewsAdapterViewHolder(itemView: View) : ViewHolder(itemView) {
        val author: TextView = itemView.findViewById(R.id.authorValue)
        val content: TextView = itemView.findViewById(R.id.content)
    }
}