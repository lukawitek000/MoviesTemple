package com.lukasz.witkowski.android.moviestemple.util


import com.lukasz.witkowski.android.moviestemple.database.entities.MovieAllInformation
import com.lukasz.witkowski.android.moviestemple.api.responses.*
import com.lukasz.witkowski.android.moviestemple.models.*



fun MovieGeneralInfoResponse.toMovie(): Movie {
    return Movie(posterPath, id, originalTitle, title, voteAverage, voteCount,  overview, releaseDate)
}


fun ReviewResponse.toReview(): Review {
    return Review(author, content)
}

fun VideoResponse.toVideo(): Video {
    return Video(key, name, site)
}


fun MovieAllInformation.toMovie(): Movie {
    val videos = videos.map { it.toVideo() }
    val reviews = reviews.map { it.toReview() }
    return Movie(movie.posterPath, movie.movieId, movie.originalTitle, movie.title, movie.voteAverage, movie.voteCount, movie.overview,
    movie.releaseDate, genres.toGenreList(), videos, reviews, cast.toActorList().sortByOrder(), directors.toDirectorList(), writers.toWriterList())
}

fun List<Actor>.sortByOrder(): List<Actor>{
    return this.sortedBy {
        it.order
    }
}


fun List<NameInterface>.toText(): String {
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

fun ActorResponse.toActor(): Actor {
    return Actor(id, name, profilePath, character, order)
}


fun CrewMemberResponse.toDirector(): Director {
    return Director(id, name, profilePath)
}

fun CrewMemberResponse.toWriter(): Writer {
    return Writer(id, name, profilePath)
}

fun List<GenreResponse>.toGenreList(): List<Genre>{
    return this.map {
        it.toGenre()
    }
}


fun GenreResponse.toGenre(): Genre {
    return Genre(genreId, name)
}

fun List<MovieGeneralInfoResponse>.toMovieList(): List<Movie>{
    return this.map {
        it.toMovie()
    }
}