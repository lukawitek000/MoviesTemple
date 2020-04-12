package com.example.android.popularmovies.database;


import android.net.Uri;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.android.popularmovies.Review;

@Entity(tableName = "Favorite_Movies")
public class MovieEntity {

    @PrimaryKey(autoGenerate = true)
    private int database_id;
    private int id;
    @ColumnInfo(name = "original_title")
    private String originalTitle;
    private String title;
    @ColumnInfo(name = "poster_uri")
    private Uri posterUri;
    private String overview;
    @ColumnInfo(name = "vote_average")
    private float voteAverage;
    @ColumnInfo(name = "release_date")
    private String releaseDate;
    @ColumnInfo(name = "video_urls")
    private String[] videoUrls;
    private Review[] reviews;



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(float voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String[] getVideoUrls() {
        return videoUrls;
    }

    public void setVideoUrls(String[] videoUrls) {
        this.videoUrls = videoUrls;
    }

    public Review[] getReviews() {
        return reviews;
    }

    public void setReviews(Review[] reviews) {
        this.reviews = reviews;
    }


    public Uri getPosterUri() {
        return posterUri;
    }

    public void setPosterUri(Uri posterUri) {
        this.posterUri = posterUri;
    }

    public int getDatabase_id() {
        return database_id;
    }

    public void setDatabase_id(int database_id) {
        this.database_id = database_id;
    }
}
