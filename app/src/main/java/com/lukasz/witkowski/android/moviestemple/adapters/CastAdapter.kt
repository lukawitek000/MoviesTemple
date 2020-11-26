package com.lukasz.witkowski.android.moviestemple.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lukasz.witkowski.android.moviestemple.R
import com.lukasz.witkowski.android.moviestemple.databinding.CastListItemBinding
import com.lukasz.witkowski.android.moviestemple.models.Actor

class CastAdapter: RecyclerView.Adapter<CastAdapter.CastViewHolder>() {


    private var castList: List<Actor> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cast_list_item, parent)
        val binding = CastListItemBinding.bind(view)
        return CastViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        val actor = castList[position]
        holder.bind(actor)
    }

    override fun getItemCount(): Int {
        return castList.size
    }




    inner class CastViewHolder(private val binding: CastListItemBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(actor: Actor){
            Glide.with(binding.actorImageview.context)
                    .load(actor.actorPhoto)
                    .placeholder(R.drawable.actor_photo_default)
                    .into(binding.actorImageview)

            binding.actorNameTextview.text = actor.name
            binding.characterNameTextView.text = actor.character
        }
    }


}