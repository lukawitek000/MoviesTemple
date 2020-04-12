package com.example.android.popularmovies.utilities;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtil {

    private static final String URL_ADDRESS = "https://api.themoviedb.org/3/movie/";
    private static final String FORMAT = "mode";
    private static final String format = "json";
    private static final String API_KEY = "api_key";
    private static final String api_key = "3b623a17f57eb4da612b3871d3f78ced";


    public static URL buildUrl(String query){
        String urlAddress = URL_ADDRESS + query;
        Uri builtUri = Uri.parse(urlAddress).buildUpon()
                .appendQueryParameter(FORMAT, format)
                .appendQueryParameter(API_KEY, api_key)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString()   );
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

}
