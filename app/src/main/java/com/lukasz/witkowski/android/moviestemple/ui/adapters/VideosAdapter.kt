package com.lukasz.witkowski.android.moviestemple.adapters


import androidx.recyclerview.widget.RecyclerView
import com.lukasz.witkowski.android.moviestemple.adapters.VideosAdapter.VideosAdapterHolder
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.lukasz.witkowski.android.moviestemple.R
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import android.widget.TextView
import com.lukasz.witkowski.android.moviestemple.models.Video

class VideosAdapter(private val videoClickListener: VideoClickListener) : RecyclerView.Adapter<VideosAdapterHolder>() {

    interface VideoClickListener {
        fun onVideoClicked(video: Video)
    }

    var videos: List<Video> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideosAdapterHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.videos_list_item, parent, false)
        return VideosAdapterHolder(view)
    }

    override fun onBindViewHolder(holder: VideosAdapterHolder, position: Int) {
        val video = videos[position]
        holder.bind(video)
    }

    override fun getItemCount(): Int {
        return videos.size
    }

    inner class VideosAdapterHolder(itemView: View) : ViewHolder(itemView), View.OnClickListener {
        private val videosText: TextView = itemView.findViewById(R.id.tv_video_label)
        var video: Video? = null

        fun bind(video: Video){
            this.video = video
            videosText.text = video.name
        }

        override fun onClick(v: View) {
            videoClickListener.onVideoClicked(video!!)
        }

        init {
            itemView.setOnClickListener(this)
        }
    }
}