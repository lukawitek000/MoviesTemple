package com.example.android.popularmovies.models

data class VideoResponse(
        var id: Long,
        var results: List<Video>
)