package com.austinevick.movieapp.ui.moviedetail

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.austinevick.movieapp.common.hasInternetConnection
import com.austinevick.movieapp.database.MovieEntity
import com.austinevick.movieapp.repository.MovieNetworkRepository
import com.austinevick.movieapp.model.MovieDetailResponseModel
import com.austinevick.movieapp.repository.MovieLocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val movieNetworkRepo: MovieNetworkRepository,
    private val movieLocalRepo: MovieLocalRepository,
    private val application: Application
) : ViewModel() {

    private val _movieDetailUIState =
        MutableLiveData<MovieDetailUIState>(MovieDetailUIState.Loading)
    val movieDetailUIState = _movieDetailUIState.asFlow()

    suspend fun getMovieById(id: Int) {
        if (hasInternetConnection(application)) {
            try {
                val response = movieNetworkRepo.getMovieById(id)
                Log.d("movie details", response.id.toString())
                _movieDetailUIState.value = MovieDetailUIState.Success(response)
            } catch (e: Exception) {
                _movieDetailUIState.value = MovieDetailUIState.Error(e.message.toString())
            }
        } else {
            _movieDetailUIState.value = MovieDetailUIState.Error("No Internet Connection")
        }
    }


    val favouriteMovies: Flow<List<MovieEntity>> = movieLocalRepo.getAllNotes

    fun saveMovieToFavourite(movieEntity: MovieEntity) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                movieLocalRepo.addMovies(movieEntity)
            }
        }
    }

    fun removeMovieFromFavourite(movieEntity: MovieEntity) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                movieLocalRepo.deleteMovie(movieEntity)
            }
        }
    }

}

sealed class MovieDetailUIState {
    data object Loading : MovieDetailUIState()
    data class Error(val message: String) : MovieDetailUIState()
    data class Success(val data: MovieDetailResponseModel) : MovieDetailUIState()
}
