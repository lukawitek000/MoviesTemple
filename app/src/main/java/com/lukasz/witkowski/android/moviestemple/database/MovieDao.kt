package com.lukasz.witkowski.android.moviestemple.database

import android.util.Log
import androidx.paging.PagingSource
import androidx.room.*
import com.lukasz.witkowski.android.moviestemple.models.*
import com.lukasz.witkowski.android.moviestemple.database.entities.*
import com.lukasz.witkowski.android.moviestemple.util.*

@Dao
interface MovieDao {

    @Query("SELECT * FROM Movies ORDER BY movieId DESC")
    fun getAllMoviesPagingSource(): PagingSource<Int, MovieEntity>

    @Query("SELECT * FROM Movies")
    suspend fun getAllMovies(): List<MovieEntity>

    @Transaction
    @Query("SELECT * FROM Movies WHERE movieId = :id")
    suspend fun getMovieWithVideosAndReviews(id: Int): MovieAllInformation

    @Query("SELECT EXISTS (SELECT 1 FROM Movies WHERE movieId = :id)")
    suspend fun isMovieInDatabase(id: Int): Boolean

    @Transaction
    suspend fun insert(movie: Movie){
        insertMovieWriters(movie)
        insertMovieDirectors(movie)
        insertMovieCast(movie)
        insertMovieGenres(movie)
        insertMovie(movie.toMovieEntity())
        insertMovieVideos(movie)
        insertMovieReviews(movie)
    }

    @Transaction
    suspend fun insertMovieReviews(movie: Movie) {
        movie.reviews.forEach {
            val reviewEntity = it.toReviewEntity()
            reviewEntity.movieOwnerID = movie.id.toLong()
            insertReview(reviewEntity)
        }
    }

    @Transaction
    suspend fun insertMovieVideos(movie: Movie) {
        movie.videos.forEach {
            val videoEntity = it.toVideoEntity()
            videoEntity.movieOwnerID = movie.id.toLong()
            insertVideo(videoEntity)
        }
    }

    @Transaction
    suspend fun insertMovieGenres(movie: Movie) {
        movie.genres.forEach {
            insertMovieWithGenres(MovieWithGenre(movie.id, it.genreId))
            insertGenres(it.toGenreEntity())
        }
    }

    @Transaction
    suspend fun insertMovieCast(movie: Movie) {
        movie.cast.forEach {
            insertMovieWithActor(MovieWithActor(movie.id, it.actorId))
            insertActor(it.toActorEntity())
        }
    }

    @Transaction
    suspend fun insertMovieDirectors(movie: Movie) {
        movie.directors.forEach {
            insertMovieWithDirector(MovieWithDirector(movie.id, it.directorId))
            insertDirector(it.toDirectorEntity())
        }
    }

    @Transaction
    suspend fun insertMovieWriters(movie: Movie){
        movie.writers.forEach {
            insertMovieWithWriter(MovieWithWriter(movie.id, it.writerId))
            insertWriter(it.toWriterEntity())
        }
    }


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertWriter(writer: WriterEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMovieWithWriter(movieWithWriter: MovieWithWriter)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDirector(director: DirectorEntity)


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMovieWithDirector(movieWithDirector: MovieWithDirector)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertActor(actor: ActorEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMovieWithActor(movieWithActor: MovieWithActor)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertGenres(genre: GenreEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMovieWithGenres(movieWithGenres: MovieWithGenre)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: ReviewEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideo(video: VideoEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: MovieEntity)


    @Transaction
    suspend fun deleteMovieReviewAndVideo(movie: MovieEntity){
        Log.i("Dao", "movie to delete = $movie")
        deleteMovieById(movie.movieId)
        deleteReviewByMovieOwnerId(movie.movieId)
        deleteVideoByMovieOwnerId(movie.movieId)
    }

    @Query("DELETE FROM Movies WHERE movieId = :id")
    suspend fun deleteMovieById(id: Int)

    @Query("DELETE FROM Reviews WHERE movieOwnerID = :id")
    suspend fun deleteReviewByMovieOwnerId(id: Int)

    @Query("DELETE FROM Videos WHERE movieOwnerID = :id")
    suspend fun deleteVideoByMovieOwnerId(id: Int)


    @Transaction
    suspend fun deleteAll(){
        deleteAllReviews()
        deleteAllVideos()
        deleteAllMovies()
    }

    @Query("DELETE FROM Movies")
    suspend fun deleteAllMovies()

    @Query("DELETE FROM Reviews")
    suspend fun deleteAllReviews()

    @Query("DELETE FROM Videos")
    suspend fun deleteAllVideos()
}