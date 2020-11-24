package com.lukasz.witkowski.android.moviestemple.models

data class VideoResponse(
        @Transient
        var id: Long = 0L,
        var results: List<Video>
)