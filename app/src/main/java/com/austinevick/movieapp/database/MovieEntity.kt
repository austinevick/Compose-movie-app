package com.austinevick.movieapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "movie_table")
data class MovieEntity(
    @PrimaryKey
    val id: Int,
    val overview: String,
    val posterPath: String,
    val releaseDate: String,
    val title: String)