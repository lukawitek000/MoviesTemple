package com.lukasz.witkowski.android.moviestemple.util

import com.lukasz.witkowski.android.moviestemple.database.entities.*
import com.lukasz.witkowski.android.moviestemple.models.*

fun Movie.toMovieEntity(): MovieEntity {
    return MovieEntity(posterPath, id, originalTitle, title, voteAverage, voteCount, overview, releaseDate)
}

fun VideoEntity.toVideo(): Video {
    return Video(key, name, site)
}

fun Video.toVideoEntity(): VideoEntity {
    return VideoEntity(0L, 0L, key, name, site)
}

fun MovieEntity.toMovie() : Movie {
    return Movie(posterPath, movieId, originalTitle, title, voteAverage, voteCount, overview, releaseDate)
}


fun Review.toReviewEntity(): ReviewEntity{
    return ReviewEntity(0L, 0L, author, content)
}


fun ReviewEntity.toReview(): Review {
    return  Review(author, content)
}


fun GenreEntity.toGenre(): Genre {
    return Genre(genreId, name)
}


fun List<GenreEntity>.toGenreList(): List<Genre>{
    return this.map {
        it.toGenre()
    }
}

fun Genre.toGenreEntity(): GenreEntity{
    return GenreEntity(genreId, name)
}



fun DirectorEntity.toDirector(): Director {
    return Director(directorId, name, profilePath)
}

fun List<DirectorEntity>.toDirectorList(): List<Director>{
    return this.map {
        it.toDirector()
    }
}

fun Director.toDirectorEntity(): DirectorEntity{
    return DirectorEntity(directorId, name, profilePath)
}


fun WriterEntity.toWriter(): Writer {
    return Writer(writerId, name, profilePath)
}

fun List<WriterEntity>.toWriterList(): List<Writer>{
    return this.map {
        it.toWriter()
    }
}


fun Writer.toWriterEntity(): WriterEntity{
    return WriterEntity(writerId, name, profilePath)
}


fun ActorEntity.toActor(): Actor {
    return Actor(actorId, name, profilePath, character, order)
}

fun List<ActorEntity>.toActorList(): List<Actor>{
    return this.map {
        it.toActor()
    }
}

fun Actor.toActorEntity(): ActorEntity{
    return ActorEntity(actorId, name, profilePath, character, order)
}




