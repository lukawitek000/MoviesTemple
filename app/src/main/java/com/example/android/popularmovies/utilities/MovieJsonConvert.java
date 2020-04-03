package com.example.android.popularmovies.utilities;

import android.net.Uri;
import com.example.android.popularmovies.Movie;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MovieJsonConvert {

    private static final String RESULTS = "results";
    private static final String POSTER = "poster_path";
    private static final String TITLE = "title";
    private static final String ORIGINAL_TITLE = "original_title";
    private static final String OVERVIEW = "overview";
    private static final String VOTE_AVERAGE = "vote_average";
    private static final String RELEASE_DATE = "release_date";
    private static final String ID = "id";



    public static Movie[] getMovieFromJson(String movieResponse) throws JSONException {
        Movie[] movies = null;

        JSONObject jsonObject = new JSONObject(movieResponse);

        if(jsonObject.has(RESULTS)){
            JSONArray resultsArray = jsonObject.getJSONArray(RESULTS);
            movies = new Movie[resultsArray.length()];

            for(int i=0; i<resultsArray.length(); i++){
                JSONObject currentMovie = resultsArray.getJSONObject(i);
                Movie movie = new Movie();
                if(currentMovie.has(ID)){
                    movie.setId(currentMovie.getInt(ID));
                }
                if(currentMovie.has(TITLE)){
                    movie.setTitle(currentMovie.getString(TITLE));
                }
                if(currentMovie.has(ORIGINAL_TITLE)){
                    movie.setOriginalTitle(currentMovie.getString(ORIGINAL_TITLE));
                }
                if(currentMovie.has(POSTER)){
                    movie.setPoster(convertPosterStringToUri(currentMovie.getString(POSTER)));
                }
                if(currentMovie.has(OVERVIEW)){
                    movie.setOverview(currentMovie.getString(OVERVIEW));
                }
                if(currentMovie.has(VOTE_AVERAGE)){
                    movie.setVoteAverage((float) currentMovie.getDouble(VOTE_AVERAGE));
                }
                if(currentMovie.has(RELEASE_DATE)){
                    movie.setReleaseDate(currentMovie.getString(RELEASE_DATE));
                }
                movies[i] = movie;
            }
        }
        return movies;
    }


    private static Uri convertPosterStringToUri(String s){
        String baseURL = "http://image.tmdb.org/t/p/w342";
        String imageURL = baseURL + s;
        return Uri.parse(imageURL).buildUpon().build();
    }








}
