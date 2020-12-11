package com.lukasz.witkowski.android.moviestemple.models

import com.lukasz.witkowski.android.moviestemple.models.entities.ActorEntity
import com.lukasz.witkowski.android.moviestemple.models.entities.DirectorEntity
import com.lukasz.witkowski.android.moviestemple.models.entities.GenreEntity
import com.lukasz.witkowski.android.moviestemple.models.entities.WriterEntity


fun GenreEntity.toGenre(): Genre{
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



fun DirectorEntity.toDirector(): Director{
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


fun WriterEntity.toWriter(): Writer{
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


fun ActorEntity.toActor(): Actor{
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


fun List<Actor>.sortByOrder(): List<Actor>{
    return this.sortedBy {
        it.order
    }
}

