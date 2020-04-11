package com.example.android.popularmovies;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.OutputStream;


public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesAdapterViewHolder> {

    public interface MovieAdapterOnClickHandler{
        void onClick(Movie movie);
    }


    private final MovieAdapterOnClickHandler clickHandler ;
    private static Movie[] moviesData;

    MoviesAdapter(MovieAdapterOnClickHandler onClickHandler){
        this.clickHandler = onClickHandler;
    }
    private Context context;

    @NonNull
    @Override
    public MoviesAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.movies_list_item, parent, false);

        return new MoviesAdapterViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final MoviesAdapterViewHolder holder, int position) {
        Movie singleMovie = moviesData[position];
        /*if(singleMovie.getPosterBitmap() != null) {
            Log.i("MoviesAdapter", " Bitmap cooooooooooool");
            holder.posterImage.setImageBitmap(singleMovie.getPosterBitmap());
        }else{
            Log.i("MoviesAdapter", "null Bitmap fuuuuck");
            holder.posterImage.setImageBitmap(null);
        }*/
        Picasso.with(holder.posterImage.getContext())
                .load(singleMovie.getPoster())
                .into(holder.posterImage);


       /* try {
            Uri uri = Uri.fromFile(File.createTempFile("temp_file_name", ".jpg", context.getCacheDir()));
            OutputStream outputStream = context.getContentResolver().openOutputStream(uri);
            singleMovie.getPosterBitmap().compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.close();
            Picasso.with(holder.posterImage.getContext())
                    .load(uri)
                    .into(holder.posterImage);
        } catch (Exception e) {
            Log.e("LoadBitmapByPicasso", e.getMessage());
        }*/
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
            Log.i("MoviesAdapter", "onClick");
            int adapterPosition = getAdapterPosition();
            clickHandler.onClick(moviesData[adapterPosition]);
        }
    }


}
