package com.austinevick.movieapp.ui.search

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.austinevick.movieapp.common.hasInternetConnection
import com.austinevick.movieapp.common.noInternetConnection
import com.austinevick.movieapp.model.MovieResponseData
import com.austinevick.movieapp.model.MovieResponseModel
import com.austinevick.movieapp.repository.MovieNetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchScreenViewModel @Inject constructor(
    private val movieNetworkRepo: MovieNetworkRepository,
    private val application: Application
) : ViewModel() {
    private val _searchMoviesUIState =
        MutableStateFlow<MovieSearchUIState>(MovieSearchUIState.None)
    val searchMoviesUIState = _searchMoviesUIState.asStateFlow()
    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()


    fun setSearch(value:String){
        _query.value = value
        viewModelScope.launch {
            delay(1000)
            searchMovies(value)
        }
    }
    fun clearSearch(){
        _query.value=""
    }

    private suspend fun searchMovies(query: String) {
        if (hasInternetConnection(application)) {
            try {
                _searchMoviesUIState.value = MovieSearchUIState.Loading
                val response = movieNetworkRepo.searchMovies(query)
                _searchMoviesUIState.value = MovieSearchUIState.Success(response.results)

            } catch (e: Exception) {
                _searchMoviesUIState.value = MovieSearchUIState.Error(e.message.toString())
            }
        } else {
            _searchMoviesUIState.value = MovieSearchUIState.Error(noInternetConnection)
        }
    }

}

sealed class MovieSearchUIState {
    data object None : MovieSearchUIState()
    data object Loading : MovieSearchUIState()
    data class Error(val message: String) : MovieSearchUIState()
    data class Success(val data:List<MovieResponseData>) : MovieSearchUIState()
}
