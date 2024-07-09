package com.austinevick.movieapp.repository

import com.austinevick.movieapp.model.MovieDetailResponseModel
import com.austinevick.movieapp.model.MovieResponseModel
import com.austinevick.movieapp.model.VideoResponseModel
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieNetworkRepository {

    @GET("trending/movie/{time_window}")
    suspend fun getTrendingMovies(@Path("time_window") time: String): MovieResponseModel

    @GET("movie/{movie_id}")
    suspend fun getMovieById(@Path("movie_id") id: Int): MovieDetailResponseModel

    @GET("movie/{movie_id}/videos")
    suspend fun getVideos(@Path("movie_id") id: Int): VideoResponseModel

    @GET("search/movie")
    suspend fun searchMovies(@Query("query") query: String): MovieResponseModel

}