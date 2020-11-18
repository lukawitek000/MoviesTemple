package com.example.android.popularmovies.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.example.android.popularmovies.adapters.VideosAdapter.VideosAdapterHolder
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.example.android.popularmovies.R
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import android.widget.TextView
import com.example.android.popularmovies.models.Video

class VideosAdapter(private val videos: List<Video>, private val videoClickListener: VideoClickListener) : RecyclerView.Adapter<VideosAdapterHolder>() {

    interface VideoClickListener {
        fun onVideoClicked(video: Video)
    }

    private var context: Context? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideosAdapterHolder {
        context = parent.context
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.videos_list_item, parent, false)
        return VideosAdapterHolder(view)
    }

    override fun onBindViewHolder(holder: VideosAdapterHolder, position: Int) {
        val video = videos[position]
        holder.videosText.text = video.name
    }

    override fun getItemCount(): Int {
        return videos.size
    }

    inner class VideosAdapterHolder(itemView: View) : ViewHolder(itemView), View.OnClickListener {
        val videosText: TextView = itemView.findViewById(R.id.video_label)

        override fun onClick(v: View) {
            val adapterPosition = adapterPosition
            videoClickListener.onVideoClicked(videos[adapterPosition])
        }

        init {
            itemView.setOnClickListener(this)
        }
    }
}