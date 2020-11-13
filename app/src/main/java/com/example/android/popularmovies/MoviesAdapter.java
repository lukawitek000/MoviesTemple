package com.example.android.popularmovies;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.popularmovies.models.Movie;
import com.squareup.picasso.Picasso;


public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesAdapterViewHolder> {

    public interface MovieAdapterOnClickHandler{
        void onClick(Movie movie);
    }


    private final MovieAdapterOnClickHandler clickHandler ;
    private static Movie[] moviesData;

    MoviesAdapter(MovieAdapterOnClickHandler onClickHandler){
        this.clickHandler = onClickHandler;
    }

    @NonNull
    @Override
    public MoviesAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.movies_list_item, parent, false);
        return new MoviesAdapterViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final MoviesAdapterViewHolder holder, int position) {
        Movie singleMovie = moviesData[position];
        Picasso.with(holder.posterImage.getContext())
                .load(singleMovie.getPoster())
                .into(holder.posterImage);

    }

    @Override
    public int getItemCount() {
        if(moviesData != null){
            return moviesData.length;
        }
        return 0;
    }

    void setMoviesData(Movie[] movies){
        moviesData = movies;
        notifyDataSetChanged();
    }

    static Movie[] getMoviesData(){return moviesData;}


    class MoviesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView posterImage;

        MoviesAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            posterImage = itemView.findViewById(R.id.poster_image_view);
            WindowManager windowManager = (WindowManager) itemView.getContext().getSystemService(Context.WINDOW_SERVICE);
            Display display;
            if (windowManager != null) {
                display = windowManager.getDefaultDisplay();
                Point point = new Point();
                display.getSize(point);
                int display_width = point.x;
                posterImage.setMinimumWidth(display_width);
            }
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            clickHandler.onClick(moviesData[adapterPosition]);
        }
    }


}
