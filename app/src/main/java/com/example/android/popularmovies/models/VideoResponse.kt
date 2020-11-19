package com.example.android.popularmovies.models

data class VideoResponse(
        @Transient
        var id: Long = 0L,
        var results: List<Video>
)