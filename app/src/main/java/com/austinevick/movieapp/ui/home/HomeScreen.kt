package com.austinevick.movieapp.ui.home

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.austinevick.movieapp.Routes
import com.austinevick.movieapp.common.MOVIE_KEY
import com.austinevick.movieapp.composable.MovieCard
import com.austinevick.movieapp.model.MovieResponseData
import com.google.gson.Gson

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope
) {
    val viewModel: HomeViewModel = hiltViewModel()

    val uiState by viewModel.uiState.collectAsState(initial = emptyList<MovieResponseData>())

    Scaffold(topBar = {
        TopAppBar(title = { Text(text = "Trending Movies") },
            actions = {
                IconButton(onClick = { navController.navigate(Routes.SEARCH.name) }) {
                    Icon(Icons.Default.Search, contentDescription = null)
                }
                IconButton(onClick = { navController.navigate(Routes.FAVOURITE.name) }) {
                    Icon(Icons.Default.FavoriteBorder, contentDescription = null)
                }

            }
        )
    }) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            when (uiState) {
                is MovieUIState.Loading -> {
                    Box(
                        modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is MovieUIState.Success -> {
                    val data = uiState as MovieUIState.Success
                    MovieListCard(
                        data.data, navController,
                        sharedTransitionScope, animatedContentScope
                    )
                }
                is MovieUIState.Error -> {
                    Text(text = (uiState as MovieUIState.Error).message)
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun MovieListCard(
    movies: List<MovieResponseData>,
    navController: NavHostController,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope
) {

    LazyVerticalGrid(columns = GridCells.Fixed(2)) {
        items(movies.size) { i ->
            movies[i].posterPath?.let {
                MovieCard(
                    imageUrl = it,
                    title = movies[i].title,
                    releaseDate = movies[i].releaseDate,
                    sharedTransitionScope = sharedTransitionScope,
                    animatedContentScope = animatedContentScope
                ) {
                    val data = Gson().toJson(movies[i].id)
                    navController.navigate("${Routes.DETAIL.name}?$MOVIE_KEY=$data")
                }
            }

        }

    }
}
