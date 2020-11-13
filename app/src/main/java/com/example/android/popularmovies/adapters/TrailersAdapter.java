package com.example.android.popularmovies.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.popularmovies.R;

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailersAdapterTrailerHolder> {

    private String[] trailers;

    private final TrailerClickListener trailerClickListener;
    public interface TrailerClickListener{
        void onTrailerClicked(String trailer);
    }

    public TrailersAdapter(TrailerClickListener trailerClickListener){
        this.trailerClickListener = trailerClickListener;
    }

    private Context context;
    @NonNull
    @Override
    public TrailersAdapterTrailerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.trailers_list_item, parent, false);
        return new TrailersAdapterTrailerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailersAdapterTrailerHolder holder, int position) {
        String text = context.getString(R.string.trailer_value, (position + 1));
        holder.trailerText.setText(text);
    }

    @Override
    public int getItemCount() {
        if(trailers != null){
            return trailers.length;
        }
        return 0;
    }

    public void setTrailers(String[] trailers) {
        this.trailers = trailers;
    }

    class TrailersAdapterTrailerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView trailerText;

        TrailersAdapterTrailerHolder(@NonNull View itemView) {
            super(itemView);
            trailerText = itemView.findViewById(R.id.trailer_label);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            trailerClickListener.onTrailerClicked(trailers[adapterPosition]);

        }
    }

}
