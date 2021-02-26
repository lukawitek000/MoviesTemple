package com.lukasz.witkowski.android.moviestemple.util


import com.google.common.truth.Truth.assertThat
import com.lukasz.witkowski.android.moviestemple.database.entities.*
import com.lukasz.witkowski.android.moviestemple.models.*
import org.junit.Test


class EntityConvertersTest{

    @Test
    fun toMovieEntity_shouldConvertMovieToMovieEntity(){

        val movie = Movie("path.com/resfd", 2, "original title","title",
                3.4f, 120, "overview long description of the movie",
                "09-09-2011")

        val movieEntity = movie.toMovieEntity()

        assertThat(movieEntity).isInstanceOf(MovieEntity::class.java)
        assertThat(movieEntity.movieId).isEqualTo(movie.id)
        assertThat(movieEntity.posterPath).isEqualTo(movie.posterPath)
        assertThat(movieEntity.originalTitle).isEqualTo(movie.originalTitle)
        assertThat(movieEntity.title).isEqualTo(movie.title)
        assertThat(movieEntity.voteAverage).isEqualTo(movie.voteAverage)
        assertThat(movieEntity.voteCount).isEqualTo(movie.voteCount)
        assertThat(movieEntity.overview).isEqualTo(movie.overview)
        assertThat(movieEntity.releaseDate).isEqualTo(movie.releaseDate)
    }

    @Test
    fun toVideo_shouldConvertVideoEntityToVideo(){
        val videoEntity = VideoEntity(1234L, 12, "key some",
                "trailer", "youtube")

        val video = videoEntity.toVideo()

        assertThat(video).isInstanceOf(Video::class.java)
        assertThat(video.key).isEqualTo(videoEntity.key)
        assertThat(video.name).isEqualTo(videoEntity.name)
        assertThat(video.site).isEqualTo(videoEntity.site)
    }

    @Test
    fun toVideoEntity_shouldConvertVideoToVideoEntity(){
        val video = Video("3ef3tg4t3gr43gr",
                "video", "youtube")

        val videoEntity = video.toVideoEntity()

        assertThat(videoEntity).isInstanceOf(VideoEntity::class.java)
        assertThat(videoEntity.key).isEqualTo(video.key)
        assertThat(videoEntity.site).isEqualTo(video.site)
        assertThat(videoEntity.name).isEqualTo(video.name)
    }


    @Test
    fun toMovie_shouldConvertMovieEntityToMovie(){
        val movieEntity = MovieEntity("qwrf23rfwe", 123, "original title",
                "title", 9.0f, 1009, "overview", "12-12-1999")

        val movie = movieEntity.toMovie()

        assertThat(movie).isInstanceOf(Movie::class.java)
        assertThat(movie.posterPath).isEqualTo(movieEntity.posterPath)
        assertThat(movie.id).isEqualTo(movieEntity.movieId)
        assertThat(movie.originalTitle).isEqualTo(movieEntity.originalTitle)
        assertThat(movie.title).isEqualTo(movieEntity.title)
        assertThat(movie.voteAverage).isEqualTo(movieEntity.voteAverage)
        assertThat(movie.voteCount).isEqualTo(movieEntity.voteCount)
        assertThat(movie.overview).isEqualTo(movieEntity.overview)
        assertThat(movie.releaseDate).isEqualTo(movieEntity.releaseDate)
        assertThat(movie.genres).isEqualTo(emptyList<Genre>())
        assertThat(movie.videos).isEqualTo(emptyList<Video>())
        assertThat(movie.reviews).isEqualTo(emptyList<Review>())
        assertThat(movie.cast).isEqualTo(emptyList<Actor>())
        assertThat(movie.directors).isEqualTo(emptyList<Director>())
        assertThat(movie.writers).isEqualTo(emptyList<Writer>())
    }


    @Test
    fun toReviewEntity_shouldConvertReviewToReviewEntity(){
        val review = Review("Twardy", "some content here")
        val reviewEntity = review.toReviewEntity()
        assertThat(reviewEntity).isInstanceOf(ReviewEntity::class.java)
        assertThat(reviewEntity.author).isEqualTo(review.author)
        assertThat(reviewEntity.content).isEqualTo(review.content)
    }


    @Test
    fun toReview_shouldConvertReviewEntityToReview(){
        val reviewEntity = ReviewEntity(123L, 1241L, "Adam", "It is super")

        val review = reviewEntity.toReview()

        assertThat(review).isInstanceOf(Review::class.java)
        assertThat(review.content).isEqualTo(reviewEntity.content)
        assertThat(review.author).isEqualTo(reviewEntity.author)
    }

    @Test
    fun toGenre_shouldConvertGenreEntityToGenre(){
        val genreEntity = GenreEntity(924, "Action")

        val genre = genreEntity.toGenre()

        assertThat(genre).isInstanceOf(Genre::class.java)
        assertThat(genre.genreId).isEqualTo(genreEntity.genreId)
        assertThat(genre.name).isEqualTo(genreEntity.name)
    }

    @Test
    fun toGenreList_shouldConvertGenreEntityListToGenreList(){
        val genreEntityList = listOf<GenreEntity>(
                GenreEntity(124, "Action"),
                GenreEntity(1, "Drama"),
                GenreEntity(42, "Action"),
                GenreEntity(154, "Action")
        )

        val genreList = genreEntityList.toGenreList()

        assertThat(genreList).contains(Genre(124, "Action"))
        assertThat(genreList).contains(Genre(1, "Drama"))
        assertThat(genreList).contains(Genre(42, "Action"))
        assertThat(genreList).contains(Genre(154, "Action"))
        assertThat(genreList.size).isEqualTo(genreEntityList.size)
    }


    @Test
    fun toGenreEntity_shouldConvertGenreToGenreEntity(){
        val genre = Genre(90, "Sci-fi")

        val genreEntity = genre.toGenreEntity()

        assertThat(genreEntity).isInstanceOf(GenreEntity::class.java)
        assertThat(genreEntity.genreId).isEqualTo(genre.genreId)
        assertThat(genreEntity.name).isEqualTo(genre.name)
    }

    @Test
    fun toDirector_shouldConvertDirectorEntityToDirector(){
        val directorEntity = DirectorEntity(1, "Dominik", "wer22")

        val director = directorEntity.toDirector()

        assertThat(director).isInstanceOf(Director::class.java)
        assertThat(director.directorId).isEqualTo(directorEntity.directorId)
        assertThat(director.name).isEqualTo(directorEntity.name)
        assertThat(director.profilePath).isEqualTo(directorEntity.profilePath)
    }

    @Test
    fun toDirectorList_shouldConvertDirectorEntityListToDirectorList(){
        val directorEntityList = listOf<DirectorEntity>(
                DirectorEntity(123, "Dominik", "sdg1"),
                DirectorEntity(5464, "Dominik", "po123"),
                DirectorEntity(23, "Dominik", "qwef123"),
                DirectorEntity(1, "Adam", null)
        )

        val directorList = directorEntityList.toDirectorList()

        assertThat(directorList.size).isEqualTo(directorEntityList.size)
        assertThat(directorList).contains(Director(123, "Dominik", "sdg1"))
        assertThat(directorList).contains(Director(5464, "Dominik", "po123"))
        assertThat(directorList).contains(Director(23, "Dominik", "qwef123"))
        assertThat(directorList).contains(Director(1, "Adam", null))
    }

    @Test
    fun toDirectorEntity_shouldConvertDirectorToDirectorEntity(){
        val director = Director(123, "Name", "rtedf23on")

        val directorEntity = director.toDirectorEntity()

        assertThat(directorEntity).isInstanceOf(DirectorEntity::class.java)
        assertThat(directorEntity.directorId).isEqualTo(director.directorId)
        assertThat(directorEntity.name).isEqualTo(director.name)
        assertThat(directorEntity.profilePath).isEqualTo(director.profilePath)
    }

    @Test
    fun toWriter_shouldConvertWriterEntityToWriter(){
        val writerEntity = WriterEntity(452, "name", "papptef")

        val writer = writerEntity.toWriter()

        assertThat(writer).isInstanceOf(Writer::class.java)
        assertThat(writer.writerId).isEqualTo(writerEntity.writerId)
        assertThat(writer.name).isEqualTo(writerEntity.name)
        assertThat(writer.profilePath).isEqualTo(writerEntity.profilePath)
    }

    @Test
    fun toWriterList_shouldConvertWriterEntityListToWriterList(){
        val writerEntityList = listOf<WriterEntity>(
                WriterEntity(452, "name", "papptef"),
                WriterEntity(123, "nnnn", "pa324f"),
                WriterEntity(653, "aaaa", "4567ef"),
                WriterEntity(4, "eeee", "aaaaaf")
        )

        val writerList = writerEntityList.toWriterList()

        assertThat(writerList).isNotEmpty()
        assertThat(writerList.size).isEqualTo(writerEntityList.size)
        assertThat(writerList).contains(Writer(452, "name", "papptef"))
        assertThat(writerList).contains(Writer(123, "nnnn", "pa324f"))
        assertThat(writerList).contains(Writer(653, "aaaa", "4567ef"))
        assertThat(writerList).contains(Writer(4, "eeee", "aaaaaf"))
    }


    @Test
    fun toWriterEntity_shouldConvertWriterToWriterEntity(){
        val writer = Writer(12, "name", "534terg")

        val writerEntity = writer.toWriterEntity()

        assertThat(writerEntity).isInstanceOf(WriterEntity::class.java)
        assertThat(writerEntity.writerId).isEqualTo(writer.writerId)
        assertThat(writerEntity.name).isEqualTo(writer.name)
        assertThat(writerEntity.profilePath).isEqualTo(writer.profilePath)
    }

    @Test
    fun toActor_shouldConvertActorEntityToActor(){
        val actorEntity = ActorEntity(324, "name", "profile path", "char", 123)

        val actor = actorEntity.toActor()

        assertThat(actor).isInstanceOf(Actor::class.java)
        assertThat(actor.actorId).isEqualTo(actorEntity.actorId)
        assertThat(actor.name).isEqualTo(actorEntity.name)
        assertThat(actor.profilePath).isEqualTo(actorEntity.profilePath)
        assertThat(actor.character).isEqualTo(actorEntity.character)
        assertThat(actor.order).isEqualTo(actorEntity.order)
    }

    @Test
    fun toActorList_shouldConvertActorEntityListToActorList(){
        val actorEntityList = listOf<ActorEntity>(
                ActorEntity(141, "nam4", "gdg463", "char", 24),
                ActorEntity(1, "nam3", "g35f", "char", 4),
                ActorEntity(2, "nam2", "g324sf", "ch", 2),
                ActorEntity(5, "nam1", "gdgs34", "ar", 1)
        )

        val actorList = actorEntityList.toActorList()

        assertThat(actorList.size).isEqualTo(actorEntityList.size)
        assertThat(actorList).contains(Actor(141, "nam4", "gdg463", "char", 24))
        assertThat(actorList).contains(Actor(1, "nam3", "g35f", "char", 4))
        assertThat(actorList).contains(Actor(2, "nam2", "g324sf", "ch", 2))
        assertThat(actorList).contains(Actor(5, "nam1", "gdgs34", "ar", 1))
    }

    @Test
    fun toActorEntity_shouldConvertActorToActorEntity(){
        val actor = Actor(432, "name", "534gr", "ch", 2151)

        val actorEntity = actor.toActorEntity()

        assertThat(actorEntity).isInstanceOf(ActorEntity::class.java)
        assertThat(actorEntity.actorId).isEqualTo(actor.actorId)
        assertThat(actorEntity.character).isEqualTo(actor.character)
        assertThat(actorEntity.name).isEqualTo(actor.name)
        assertThat(actorEntity.order).isEqualTo(actor.order)
        assertThat(actorEntity.profilePath).isEqualTo(actor.profilePath)
    }

}