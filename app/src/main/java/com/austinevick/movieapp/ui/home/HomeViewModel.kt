package com.austinevick.movieapp.ui.home

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.austinevick.movieapp.repository.MovieNetworkRepository
import com.austinevick.movieapp.model.MovieResponseData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val movieNetworkRepository: MovieNetworkRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _savedFilterType =
        savedStateHandle.getStateFlow(MOVIE_FILTER_SAVED_STATE_KEY, TimeWindow.day)

    private val _uiState = MutableLiveData<MovieUIState>(MovieUIState.Loading)
    val uiState = _uiState.asFlow()
    private val _time = mutableStateOf(TimeWindow.day)
    val time: State<TimeWindow> = _time


    init {
        viewModelScope.launch {
            getTrendingMovies()
        }
    }

    private suspend fun getTrendingMovies() {
        try {
            val response = movieNetworkRepository.getTrendingMovies(time = _savedFilterType.value.name)
            _uiState.value = MovieUIState.Success(data = response.results)
            Log.d("asd", response.results.size.toString())
        } catch (e: Exception) {
            _uiState.value = MovieUIState.Error(e.message.toString())
            Log.d("error", e.toString())

        }
    }

    fun setFiltering(requestType: TimeWindow) {
        savedStateHandle[MOVIE_FILTER_SAVED_STATE_KEY] = requestType
    }


}

// Used to save the current filtering in SavedStateHandle.
const val MOVIE_FILTER_SAVED_STATE_KEY = "MOVIE_FILTER_SAVED_STATE_KEY"

enum class TimeWindow {
    day, week
}

sealed class MovieUIState {
    data object Loading : MovieUIState()
    data class Error(val message: String) : MovieUIState()
    data class Success(val data: List<MovieResponseData>) : MovieUIState()
}
