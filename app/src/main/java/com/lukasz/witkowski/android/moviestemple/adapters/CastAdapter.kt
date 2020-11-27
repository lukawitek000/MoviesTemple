package com.lukasz.witkowski.android.moviestemple.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lukasz.witkowski.android.moviestemple.R
import com.lukasz.witkowski.android.moviestemple.databinding.CastListItemBinding
import com.lukasz.witkowski.android.moviestemple.models.Actor

class CastAdapter(private val onClickListener: CastOnClickListener): RecyclerView.Adapter<CastAdapter.CastViewHolder>() {


    interface CastOnClickListener{
        fun onClick(actor: Actor)
    }


    private var castList: List<Actor> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder {
        val binding = CastListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CastViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        val actor = castList[position]
        holder.bind(actor)
    }

    override fun getItemCount(): Int {
        return castList.size
    }

    fun setCastAdapterList(cast: List<Actor>){
        castList = cast
    }


    inner class CastViewHolder(private val binding: CastListItemBinding): RecyclerView.ViewHolder(binding.root),
    View.OnClickListener{

        init {
            binding.root.setOnClickListener(this)
        }

        private var actor: Actor? = null

        fun bind(actor: Actor){
            Glide.with(binding.actorImageview.context)
                    .load(actor.actorPhoto)
                    .placeholder(R.drawable.actor_photo_default)
                    .into(binding.actorImageview)

            binding.actorNameTextview.text = actor.name
            binding.characterNameTextView.text = actor.character
            this.actor = actor
        }

        override fun onClick(p0: View?) {
            if(actor != null){
                onClickListener.onClick(actor!!)
            }
        }
    }


}