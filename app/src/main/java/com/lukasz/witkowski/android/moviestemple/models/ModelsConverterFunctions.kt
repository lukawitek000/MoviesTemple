package com.lukasz.witkowski.android.moviestemple.models

import com.lukasz.witkowski.android.moviestemple.models.entities.MovieEntity
import com.lukasz.witkowski.android.moviestemple.models.entities.MovieAllInformation
import com.lukasz.witkowski.android.moviestemple.models.entities.ReviewEntity
import com.lukasz.witkowski.android.moviestemple.models.entities.VideoEntity
import com.lukasz.witkowski.android.moviestemple.models.responses.*


fun Movie.toMovieEntity(): MovieEntity{
    return MovieEntity(posterPath, id, originalTitle, title, voteAverage, voteCount, overview, releaseDate)
}

fun MovieGeneralInfoResponse.toMovie(): Movie {
    return Movie(posterPath, id, originalTitle, title, voteAverage, voteCount,  overview, releaseDate)
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

fun MovieAllInformation.toMovie(): Movie{
    val videos = videos.map { it.toVideo() }
    val reviews = reviews.map { it.toReview() }
    return Movie(movie.posterPath, movie.movieId, movie.originalTitle, movie.title, movie.voteAverage, movie.voteCount, movie.overview,
    movie.releaseDate, genres, videos, reviews, cast.sortedBy {
        it.order
    }, directors)
}


fun MovieEntity.toMovie() : Movie {
    return Movie(posterPath, movieId, originalTitle, title, voteAverage, voteCount, overview, releaseDate)
}

fun List<Genre>.toText(): String {
    val textBuilder = StringBuilder()
    this.forEachIndexed{ i, genre ->

        if(i == this.size-1){
            textBuilder.append(genre.name)
        }else{
            textBuilder.append(genre.name).append(", ")
        }
    }
    return textBuilder.toString()
}

fun ActorResponse.toActor(): Actor{
    return Actor(id, name, profilePath, character, order)
}


fun CrewMemberResponse.toDirector(): Director{
    return Director(id, name, profilePath)
}

fun CrewMemberResponse.toWriter(): Writer {
    return Writer(id, name, profilePath)
}


fun List<Director>.directorToString(): String {
    val textBuilder = StringBuilder()
    this.forEachIndexed{ i, director ->

        if(i == this.size-1){
            textBuilder.append(director.name)
        }else{
            textBuilder.append(director.name).append(", ")
        }
    }
    return textBuilder.toString()
}

fun List<Writer>.writerToString(): String {
    val textBuilder = StringBuilder()
    this.forEachIndexed{ i, writer ->

        if(i == this.size-1){
            textBuilder.append(writer.name)
        }else{
            textBuilder.append(writer.name).append(", ")
        }
    }
    return textBuilder.toString()
}