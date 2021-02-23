package com.lukasz.witkowski.android.moviestemple.util

import com.google.common.truth.Truth.assertThat
import com.lukasz.witkowski.android.moviestemple.api.responses.*
import com.lukasz.witkowski.android.moviestemple.database.entities.*
import com.lukasz.witkowski.android.moviestemple.models.*
import org.junit.Test


class ApiResponsesConvertersTest{


    @Test
    fun toMovie_shouldConvertMovieGeneralInfoResponseToMovie(){

        val movieGeneralInfoResponse = MovieGeneralInfoResponse(
                "Poster path", false, "some overview",
                "release date here", emptyList(), 23, "original title",
                "original language", "title", "backdropPath",
                1.6f, 1999, false, 5.5f)


        val movie = movieGeneralInfoResponse.toMovie()
       /* var posterPath: String? = "",
        var id: Int = 0,
        var originalTitle: String = "",
        var title: String,
        var voteAverage: Float = 0f,
        var voteCount: Int = 0,
        var overview: String = "",
        var releaseDate: String = "",
        var genres: List<Genre> = emptyList(),
        var videos: List<Video> = emptyList(),
        var reviews: List<Review> = emptyList(),
        var cast: List<Actor> = emptyList(),
        var directors: List<Director> = emptyList(),
        var writers: List<Writer> = emptyList()*/
        assertThat(movie.posterPath).isEqualTo(movieGeneralInfoResponse.posterPath)
        assertThat(movie.id).isEqualTo(movieGeneralInfoResponse.id)
        assertThat(movie.originalTitle).isEqualTo(movieGeneralInfoResponse.originalTitle)
        assertThat(movie.title).isEqualTo(movieGeneralInfoResponse.title)
        assertThat(movie.voteAverage).isEqualTo(movieGeneralInfoResponse.voteAverage)
        assertThat(movie.voteCount).isEqualTo(movieGeneralInfoResponse.voteCount)
        assertThat(movie.overview).isEqualTo(movieGeneralInfoResponse.overview)
        assertThat(movie.releaseDate).isEqualTo(movieGeneralInfoResponse.releaseDate)

        assertThat(movie.genres).isEqualTo(emptyList<Genre>())
        assertThat(movie.videos).isEqualTo(emptyList<Video>())
        assertThat(movie.reviews).isEqualTo(emptyList<Review>())
        assertThat(movie.cast).isEqualTo(emptyList<Actor>())
        assertThat(movie.directors).isEqualTo(emptyList<Director>())
        assertThat(movie.writers).isEqualTo(emptyList<Writer>())
    }


    @Test
    fun toReview_shouldConvertReviewResponseToReview(){
        //GIVEN
        val reviewResponse = ReviewResponse(
                "Some author", "content random",
                "1234", "url.com/a2s4d6gw323gv")
        //WHEN
        val response = reviewResponse.toReview()
        //THEN
        assertThat(response.author).isEqualTo(reviewResponse.author)
        assertThat(response.content).isEqualTo(reviewResponse.content)
    }


    @Test
    fun toVideo_shouldConvertVideoResponseToVideo(){
        val videoResponse = VideoResponse(
                "1241", "iso1", "iso2", "key",
                "name", "youtube",  123, "trailer")

        val video = videoResponse.toVideo()

        assertThat(video.key).isEqualTo(videoResponse.key)
        assertThat(video.name).isEqualTo(videoResponse.name)
        assertThat(video.site).isEqualTo(videoResponse.site)
    }


    @Test
    fun toMovie_shouldConvertMovieAllInformationToMovie(){

        val movieAllInformation = MovieAllInformation(
                MovieEntity(
                        "poster path", 12, "original title",
                        "title", 4.4f, 1090, "overview",
                        "12-01-2000"),
                emptyList<ActorEntity>(),
                emptyList<GenreEntity>(),
                emptyList<ReviewEntity>(),
                emptyList<VideoEntity>(),
                emptyList<DirectorEntity>(),
                emptyList<WriterEntity>()
        )
        val movie = movieAllInformation.toMovie()

        assertThat(movie.posterPath).isEqualTo(movieAllInformation.movie.posterPath)
        assertThat(movie.id).isEqualTo(movieAllInformation.movie.movieId)

        assertThat(movie.originalTitle).isEqualTo(movieAllInformation.movie.originalTitle)
        assertThat(movie.title).isEqualTo(movieAllInformation.movie.title)

        assertThat(movie.voteAverage).isEqualTo(movieAllInformation.movie.voteAverage)
        assertThat(movie.voteCount).isEqualTo(movieAllInformation.movie.voteCount)

        assertThat(movie.overview).isEqualTo(movieAllInformation.movie.overview)
        assertThat(movie.releaseDate).isEqualTo(movieAllInformation.movie.releaseDate)


        assertThat(movie.genres).isEqualTo(emptyList<Genre>())
        assertThat(movie.videos).isEqualTo(emptyList<Video>())
        assertThat(movie.reviews).isEqualTo(emptyList<Review>())
        assertThat(movie.cast).isEqualTo(emptyList<Actor>())
        assertThat(movie.directors).isEqualTo(emptyList<Director>())
        assertThat(movie.writers).isEqualTo(emptyList<Writer>())

    }


    @Test
    fun sortByOrder_shouldSortActorListByOrder(){

        val unsortedActors = listOf<Actor>(
                Actor(0, "Name 1", null, "character 1", 102),
                Actor(34, "Name 1", null, "character 1", 10),
                Actor(65, "Name 1", null, "character 1", 2),
                Actor(10, "Name 1", null, "character 1", 1202),
                Actor(1, "Name 1", null, "character 1", 1052)
        )

        val sortedActors = unsortedActors.sortByOrder()

        assertThat(sortedActors[0]).isEqualTo(unsortedActors[2])
        assertThat(sortedActors[1]).isEqualTo(unsortedActors[1])
        assertThat(sortedActors[2]).isEqualTo(unsortedActors[0])
        assertThat(sortedActors[3]).isEqualTo(unsortedActors[4])
        assertThat(sortedActors[4]).isEqualTo(unsortedActors[3])
    }

    @Test
    fun toText_shouldConvertNameInterfaceNameToString(){

        val nameInterfaceList = listOf<NameInterface>(
                Director(12, "name of director", null),
                DirectorEntity(1234, "Weronika ", "some path here"),
                Genre(321, "Action"),
                GenreEntity(654, "Comedy"),
                GenreResponse(987, "Drama"),
                Writer(98, "Name of writer", null),
                WriterEntity(456, "Adama", "url.com/r4t4y5i7")
        )

        val string = nameInterfaceList.toText()
        assertThat(string).isEqualTo(
                "name of director, Weronika , Action, Comedy, Drama, Name of writer, Adama"
        )
    }


    @Test
    fun toActor_shouldConvertActorResponseToActor(){
        /*var name: String = "",
        @Json(name = "original_name")
        var originalName: String = "",
        var popularity: Float = 0f,
        @Json(name = "profile_path")
        var profilePath: String? = null,
        @Json(name = "cast_id")
        var castId: Int = 0,
        var character: String = "",
        @Json(name = "credit_id")
        var creditId: String = "",
        var order: Int = 0*/
        val actorResponse = ActorResponse(
                false, 1, 345, "Director", "name",
                "original name", 1.6f, null, 12,
                "Toreto", "634", 90)

        val actor = actorResponse.toActor()

        assertThat(actor.actorId).isEqualTo(actorResponse.id)
        assertThat(actor.name).isEqualTo(actorResponse.name)
        assertThat(actor.profilePath).isEqualTo(actorResponse.profilePath)
        assertThat(actor.character).isEqualTo(actorResponse.character)
        assertThat(actor.order).isEqualTo(actorResponse.order)

    }

    @Test
    fun toDirector_shouldConvertCrewMemberResponseToDirector(){

        val crewMemberResponse = CrewMemberResponse(false, 1, 32,
                "director", "Adam", "Adamos", 3.4f,
                null, "q3w5", "Directors", "Director")

        val director = crewMemberResponse.toDirector()

        assertThat(director.directorId).isEqualTo(crewMemberResponse.id)
        assertThat(director.name).isEqualTo(crewMemberResponse.name)
        assertThat(director.profilePath).isEqualTo(crewMemberResponse.profilePath)
    }

    @Test
    fun toWriter_shouldConvertCrewMemberResponseToWriter(){
        val crewMemberResponse = CrewMemberResponse(false, 0, 312,
                "Writers", "Ada", "none", 4.4f,
                null, "3e5", "Writers", "Writer")
        val writer = crewMemberResponse.toWriter()
        assertThat(writer.writerId).isEqualTo(crewMemberResponse.id)
        assertThat(writer.name).isEqualTo(crewMemberResponse.name)
        assertThat(writer.profilePath).isEqualTo(crewMemberResponse.profilePath)
    }


    @Test
    fun toGenreList_shouldConvertGenreResponseListToGenreList(){

        GenreResponse(12, "Action")
        val genreResponseList = listOf<GenreResponse>(
                GenreResponse(1, "Action"),
                GenreResponse(87, "Comedy"),
                GenreResponse(42, "Drama"),
                GenreResponse(2, "Sci-fi"),
                GenreResponse(102, "12345678qwertyuijhgfdsertyu")
        )


        val genreList = genreResponseList.toGenreList()

        assertThat(genreList.size).isEqualTo(genreResponseList.size)
        for(i in genreResponseList.indices){
            assertThat(genreList[i]).isInstanceOf(Genre::class.java)
            assertThat(genreList[i].genreId).isEqualTo(genreResponseList[i].genreId)
            assertThat(genreList[i].name).isEqualTo(genreResponseList[i].name)
        }
    }

    @Test
    fun toGenre_shouldConvertGenreResponseToGenre(){
        val genreResponse = GenreResponse(12, "Drama")

        val genre = genreResponse.toGenre()

        assertThat(genre).isInstanceOf(Genre::class.java)
        assertThat(genre.genreId).isEqualTo(genreResponse.genreId)
        assertThat(genre.name).isEqualTo(genreResponse.name)
    }

    @Test
    fun toMovieList_shouldConvertMovieGeneralInfoResponseListToMovieList(){
        val movieGenreResponseList = listOf<MovieGeneralInfoResponse>(
                MovieGeneralInfoResponse("poster.com/123", true,
                        "some overview here", "12-03-1999", listOf(12L, 121L),
                        123, "original title", "eng", "title",
                        null, 3.0f, 10000, true, 9.0f),
                MovieGeneralInfoResponse("url.com/123", false,
                        "overview",
                        "11-11-1199", listOf(121L), 3, "original title",
                        "eng", "title", null, 2.0f,
                        900, true, 6.0f),
                MovieGeneralInfoResponse("poster.com/123", true,
                        "some overview here", "12-03-2009", listOf(12L, 1L),
                        1, "original title", "pl", "title",
                        null, 1.0f, 100, true, 7.0f)
        )

        val movieList = movieGenreResponseList.toMovieList()


        for(i in movieGenreResponseList.indices){
            assertThat(movieList[i]).isInstanceOf(Movie::class.java)

            assertThat(movieList[i].posterPath).isEqualTo(movieGenreResponseList[i].posterPath)

            assertThat(movieList[i].id).isEqualTo(movieGenreResponseList[i].id)
            assertThat(movieList[i].originalTitle).isEqualTo(movieGenreResponseList[i].originalTitle)
            assertThat(movieList[i].title).isEqualTo(movieGenreResponseList[i].title)
            assertThat(movieList[i].voteAverage).isEqualTo(movieGenreResponseList[i].voteAverage)
            assertThat(movieList[i].voteCount).isEqualTo(movieGenreResponseList[i].voteCount)
            assertThat(movieList[i].overview).isEqualTo(movieGenreResponseList[i].overview)
            assertThat(movieList[i].releaseDate).isEqualTo(movieGenreResponseList[i].releaseDate)
            assertThat(movieList[i].genres).isEqualTo(emptyList<Genre>())
            assertThat(movieList[i].videos).isEqualTo(emptyList<Video>())
            assertThat(movieList[i].reviews).isEqualTo(emptyList<Review>())
            assertThat(movieList[i].cast).isEqualTo(emptyList<Actor>())
            assertThat(movieList[i].directors).isEqualTo(emptyList<Director>())
            assertThat(movieList[i].writers).isEqualTo(emptyList<Writer>())
        }


    }








}