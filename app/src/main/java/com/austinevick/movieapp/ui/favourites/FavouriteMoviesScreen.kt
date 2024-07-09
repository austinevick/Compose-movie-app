package com.austinevick.movieapp.ui.favourites

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.austinevick.movieapp.Routes
import com.austinevick.movieapp.common.MOVIE_KEY
import com.austinevick.movieapp.composable.MovieCard
import com.austinevick.movieapp.ui.moviedetail.MovieDetailViewModel
import com.google.gson.Gson

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun FavouriteMoviesScreen(
    navController: NavHostController,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    viewModel: MovieDetailViewModel = hiltViewModel()
) {

    val favouriteMovies = viewModel.favouriteMovies.collectAsState(initial = emptyList())

    Scaffold(topBar = {
        TopAppBar(title = { Text(text = "Favourite Movies") })
    }) {
        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            columns = GridCells.Fixed(2)
        ) {
            items(favouriteMovies.value.size) { i ->
                MovieCard(
                    imageUrl = favouriteMovies.value[i].posterPath,
                    title = favouriteMovies.value[i].title,
                    releaseDate = favouriteMovies.value[i].releaseDate,
                    sharedTransitionScope = sharedTransitionScope,
                    animatedContentScope = animatedContentScope
                ) {
                    val data = Gson().toJson(favouriteMovies.value[i].id)
                    navController.navigate("${Routes.DETAIL.name}?$MOVIE_KEY=$data")
                }

            }
        }
    }
}