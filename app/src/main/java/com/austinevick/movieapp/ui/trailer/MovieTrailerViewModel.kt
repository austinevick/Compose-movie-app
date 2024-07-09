package com.austinevick.movieapp.ui.trailer

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import com.austinevick.movieapp.repository.MovieNetworkRepository
import com.austinevick.movieapp.model.VideoResponseData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MovieTrailerViewModel @Inject constructor(
    private val repository: MovieNetworkRepository,
) : ViewModel() {

    private val _movieTrailerUIState =
        MutableLiveData<MovieTrailerUIState>(MovieTrailerUIState.Loading)
    val movieTrailerUIState = _movieTrailerUIState.asFlow()
    private val _videoKey = MutableStateFlow("")
    val videoKey = _videoKey.asStateFlow()


    fun setVideoKey(key: String) {
        _videoKey.value = key
    }

    suspend fun getVideosByMovieId(id: Int) {
        try {
            val response = repository.getVideos(id)
            Log.d("movie details", response.id.toString())
            _movieTrailerUIState.value = MovieTrailerUIState.Success(response.results)
            //_videoKey.value = response.results.first().key
        } catch (e: Exception) {
            _movieTrailerUIState.value = MovieTrailerUIState.Error(e.message.toString())
        }
    }

}

sealed class MovieTrailerUIState {
    data object Loading : MovieTrailerUIState()
    data class Error(val message: String) : MovieTrailerUIState()
    data class Success(val data: List<VideoResponseData>) : MovieTrailerUIState()
}
