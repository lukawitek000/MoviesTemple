package com.example.android.popularmovies;

import android.net.Uri;

public class Movie {


    private int id;
    private String originalTitle;
    private String title;
    private Uri poster;
    private String overview;
    private float voteAverage;
    private String releaseDate;
    private String[] videoUrls;
    private Review[] reviews;


    public Movie(){}


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Uri getPoster() {
        return poster;
    }

    public void setPoster(Uri poster) {
        this.poster = poster;
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
}
