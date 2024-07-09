package com.austinevick.movieapp.repository

import com.austinevick.movieapp.database.MovieDao
import com.austinevick.movieapp.database.MovieEntity
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class MovieLocalRepository @Inject constructor(private val movieDao: MovieDao) {

    val getAllNotes: Flow<List<MovieEntity>> = movieDao.getAllMovies()

    suspend fun addMovies(movieEntity: MovieEntity) {
        movieDao.addMovies(movieEntity)
    }

    suspend fun deleteMovie(movieEntity: MovieEntity) {
        movieDao.deleteMovie(movieEntity)
    }

}