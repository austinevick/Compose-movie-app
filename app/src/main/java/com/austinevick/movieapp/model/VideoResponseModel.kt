package com.austinevick.movieapp.model


import com.google.gson.annotations.SerializedName

data class VideoResponseModel(
    @SerializedName("id")
    val id: Int,
    @SerializedName("results")
    val results: List<VideoResponseData>
)


data class VideoResponseData(
    @SerializedName("id")
    val id: String,
    @SerializedName("iso_3166_1")
    val iso31661: String,
    @SerializedName("iso_639_1")
    val iso6391: String,
    @SerializedName("key")
    val key: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("official")
    val official: Boolean,
    @SerializedName("published_at")
    val publishedAt: String,
    @SerializedName("site")
    val site: String,
    @SerializedName("size")
    val size: Int,
    @SerializedName("type")
    val type: String
)