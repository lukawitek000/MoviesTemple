package com.example.android.popularmovies.adapters

import com.example.android.popularmovies.models.Review.content
import com.example.android.popularmovies.models.Review.author
import androidx.recyclerview.widget.RecyclerView
import com.example.android.popularmovies.adapters.ReviewsAdapter.ReviewsAdapterViewHolder
import com.example.android.popularmovies.models.Review
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.example.android.popularmovies.R
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import android.widget.TextView

class ReviewsAdapter : RecyclerView.Adapter<ReviewsAdapterViewHolder>() {
    private var reviews: Array<Review>?
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewsAdapterViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.reviews_list_item, parent, false)
        return ReviewsAdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewsAdapterViewHolder, position: Int) {
        val (author, content) = reviews!![position]
        holder.content.text = content
        holder.author.text = author
    }

    override fun getItemCount(): Int {
        return if (reviews != null) reviews!!.size else 0
    }

    fun setReviews(reviews: Array<Review>?) {
        this.reviews = reviews
    }

    class ReviewsAdapterViewHolder(itemView: View) : ViewHolder(itemView) {
        val author: TextView
        val content: TextView

        init {
            author = itemView.findViewById(R.id.authorValue)
            content = itemView.findViewById(R.id.content)
        }
    }
}