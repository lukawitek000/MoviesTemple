package com.example.android.popularmovies.utilities

import android.net.Uri
import androidx.lifecycle.LiveData
import com.example.android.popularmovies.models.Movie
import com.example.android.popularmovies.models.TMDBResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.*


interface TMDBService{

    @GET("/3/movie/popular")
    suspend fun getPopularMovies(@Query(API_KEY) apiKey: String = api_key): TMDBResponse

    @GET("/3/movie/top_rated")
    suspend fun getTopRatedMovies(@Query(API_KEY) apiKey: String = api_key): TMDBResponse

}

//private const val URL_ADDRESS = "https://api.themoviedb.org/3/movie/"
private const val URL_ADDRESS = "https://api.themoviedb.org"
private const val FORMAT = "mode"
private const val format = "json"
private const val API_KEY = "api_key"
private const val api_key = "3b623a17f57eb4da612b3871d3f78ced"

private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(URL_ADDRESS)
        .build()

object TMDBApi{
    val retrofitService : TMDBService by lazy {
        retrofit.create(TMDBService::class.java)
    }
}


/*
object NetworkUtil {
    private const val URL_ADDRESS = "https://api.themoviedb.org/3/movie/"
    private const val FORMAT = "mode"
    private const val format = "json"
    private const val API_KEY = "api_key"
    private const val api_key = "3b623a17f57eb4da612b3871d3f78ced"


    fun buildUrl(query: String): URL? {
        val urlAddress = URL_ADDRESS + query
        val builtUri = Uri.parse(urlAddress).buildUpon()
                .appendQueryParameter(FORMAT, format)
                .appendQueryParameter(API_KEY, api_key)
                .build()
        var url: URL? = null
        try {
            url = URL(builtUri.toString())
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }
        return url
    }

    @Throws(IOException::class)
    fun getResponseFromHttpUrl(url: URL): String? {
        val urlConnection = url.openConnection() as HttpURLConnection
        return try {
            val `in` = urlConnection.inputStream
            val scanner = Scanner(`in`)
            scanner.useDelimiter("\\A")
            val hasInput = scanner.hasNext()
            if (hasInput) {
                scanner.next()
            } else {
                null
            }
        } finally {
            urlConnection.disconnect()
        }
    }
}*/