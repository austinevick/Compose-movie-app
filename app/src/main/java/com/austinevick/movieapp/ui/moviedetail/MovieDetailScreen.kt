package com.austinevick.movieapp.ui.moviedetail

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.austinevick.movieapp.common.IMAGE_URL
import com.austinevick.movieapp.common.MOVIE_KEY
import com.austinevick.movieapp.Routes
import com.austinevick.movieapp.database.MovieEntity

@OptIn(ExperimentalSharedTransitionApi::class)
@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun MovieDetailScreen(
    navController: NavHostController,
    result: Int,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    viewModel: MovieDetailViewModel = hiltViewModel()
) {
    val uiState = viewModel.movieDetailUIState
        .collectAsState(initial = MovieDetailUIState.Loading)
    val favouriteMovies = viewModel.favouriteMovies.collectAsState(initial = emptyList())


    LaunchedEffect(key1 = true) {
        viewModel.getMovieById(result)
    }

    when (uiState.value) {
        is MovieDetailUIState.Loading -> {
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is MovieDetailUIState.Success -> {
            val data = uiState.value as MovieDetailUIState.Success
            val movieEntity = MovieEntity(
                data.data.id,
                data.data.overview,
                data.data.posterPath,
                data.data.releaseDate,
                data.data.title
            )
            with(sharedTransitionScope) {
                Column(
                    modifier = Modifier
                        .windowInsetsPadding(WindowInsets(0, 0, 0, 50))
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {

                    AsyncImage(
                        model = IMAGE_URL + data.data.posterPath, "",
                        modifier = Modifier
                            .sharedElement(
                                sharedTransitionScope
                                    .rememberSharedContentState(key = data.data.posterPath),
                                animatedVisibilityScope = animatedContentScope
                            )
                    )
                    Column(
                        modifier = Modifier
                            .padding(
                                horizontal = 16.dp,
                                vertical = 20.dp
                            )
                    ) {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = data.data.title,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            IconButton(onClick = {
                                if (favouriteMovies.value.contains(movieEntity)) {
                                    viewModel.removeMovieFromFavourite(movieEntity)
                                } else {
                                    viewModel.saveMovieToFavourite(movieEntity)
                                }

                            }) {
                                if (favouriteMovies.value.contains(movieEntity)) {
                                    Icon(Icons.Default.Favorite, null, tint = Color.Red)
                                } else {
                                    Icon(Icons.Default.FavoriteBorder, null)

                                }
                            }
                        }
                        Text(text = data.data.overview, fontSize = 14.sp)
                        HorizontalDivider()
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Released: ${data.data.releaseDate}",
                                fontSize = 14.sp, color = Color.Gray
                            )
                            Text(
                                text = data.data.spokenLanguages[0].name,
                                fontSize = 14.sp, color = Color.Gray
                            )
                        }
                        Row {
                            List(data.data.genres.size) {
                                Text(
                                    text = data.data.genres[it].name,
                                    modifier = Modifier.padding(end = 4.dp),
                                    fontSize = 14.sp, color = Color.Gray
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(25.dp))
                        Button(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Red
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                navController.navigate("${Routes.TRAILER.name}?$MOVIE_KEY=${data.data.id}")
                            }) {
                            Text(text = "Watch Trailers")
                        }
                    }
                }
            }
        }

        is MovieDetailUIState.Error -> {
            Text(text = (uiState.value as MovieDetailUIState.Error).message)
        }
    }


}