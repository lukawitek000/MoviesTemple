package com.lukasz.witkowski.android.moviestemple.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lukasz.witkowski.android.moviestemple.R
import com.lukasz.witkowski.android.moviestemple.databinding.MoviesLoadStateFooterViewItemBinding

class MoviesLoadStateAdapter(private val context: Context, private val retry: () -> Unit)
    : LoadStateAdapter<MoviesLoadStateAdapter.MoviesLoadStateViewHolder>() {


    override fun onBindViewHolder(holder: MoviesLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): MoviesLoadStateViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movies_load_state_footer_view_item, parent, false)
        val binding = MoviesLoadStateFooterViewItemBinding.bind(view)
        return MoviesLoadStateViewHolder(binding, retry)
    }

    inner class MoviesLoadStateViewHolder(
            private val binding: MoviesLoadStateFooterViewItemBinding,
            retry: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root){

        init {
            binding.retryButton.setOnClickListener {
                retry.invoke()
            }
        }


        fun bind(loadState: LoadState){
            if(loadState is LoadState.Error){
                binding.errorMessage.text = context.getString(R.string.connection_error_message)
            }
            binding.moviesProgressbar.isVisible = loadState is LoadState.Loading
            binding.errorMessage.isVisible = loadState !is LoadState.Loading
            binding.retryButton.isVisible = loadState !is LoadState.Loading
        }

    }


}