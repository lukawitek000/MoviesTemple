package com.lukasz.witkowski.android.moviestemple.models

import com.lukasz.witkowski.android.moviestemple.models.entities.MovieEntity
import com.lukasz.witkowski.android.moviestemple.models.entities.MovieWithReviewsAndVideos
import com.lukasz.witkowski.android.moviestemple.models.entities.ReviewEntity
import com.lukasz.witkowski.android.moviestemple.models.entities.VideoEntity
import com.lukasz.witkowski.android.moviestemple.models.responses.MovieGeneralInfoResponse
import com.lukasz.witkowski.android.moviestemple.models.responses.ReviewResponse
import com.lukasz.witkowski.android.moviestemple.models.responses.VideoResponse


fun Movie.toMovieEntity(): MovieEntity{
    return MovieEntity(posterPath, id, originalTitle, title, voteAverage, overview, releaseDate)
}

fun MovieGeneralInfoResponse.toMovie(): Movie {
    return Movie(posterPath, id, originalTitle, title, voteAverage, overview, releaseDate)
}


fun ReviewResponse.toReview(): Review {
    return Review(author, content)
}

fun VideoResponse.toVideo(): Video{
    return Video(key, name, site)
}


fun VideoEntity.toVideo(): Video {
    return Video(key, name, site)
}

fun Video.toVideoEntity(): VideoEntity {
    return VideoEntity(0L, 0L, key, name, site)
}


fun Review.toReviewEntity(): ReviewEntity{
    return ReviewEntity(0L, 0L, author, content)
}


fun ReviewEntity.toReview(): Review{
    return  Review(author, content)
}

fun MovieWithReviewsAndVideos.toMovie(): Movie{
    val videos = videos.map { it.toVideo() }
    val reviews = reviews.map { it.toReview() }
    return Movie(movie.posterPath, movie.id, movie.originalTitle, movie.title, movie.voteAverage, movie.overview,
    movie.releaseDate, emptyList(), videos, reviews)
}


fun MovieEntity.toMovie() : Movie {
    return Movie(posterPath, id, originalTitle, title, voteAverage, overview, releaseDate)
}