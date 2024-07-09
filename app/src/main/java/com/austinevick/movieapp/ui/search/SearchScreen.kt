package com.austinevick.movieapp.ui.search

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.austinevick.movieapp.Routes
import com.austinevick.movieapp.common.IMAGE_URL
import com.austinevick.movieapp.common.MOVIE_KEY
import com.austinevick.movieapp.composable.MovieCard
import com.google.gson.Gson

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SearchScreen(
    navController: NavHostController,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope
    ) {
    val viewModel: SearchScreenViewModel = hiltViewModel()
    val uiState = viewModel.searchMoviesUIState.collectAsState()

    val searchValue = viewModel.query.collectAsState()

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
        ) {
            SearchBar(
                value = searchValue.value.trim(),
                onValueChange = viewModel::setSearch,
                onSearch = { viewModel.setSearch(searchValue.value) },
                onClearClick = viewModel::clearSearch,
                navController = navController
            )
            when(uiState.value){
                is MovieSearchUIState.Loading -> {
                    Box(Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) { CircularProgressIndicator() }
                }
                is MovieSearchUIState.Error -> {
                    Text(text = (uiState.value as MovieSearchUIState.Error).message)
                }
                is MovieSearchUIState.Success -> {
                    val movie = (uiState.value as MovieSearchUIState.Success).data


                        LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                            items(movie.size) { i ->
                                movie[i].posterPath?.let {
                                    MovieCard(
                                        imageUrl = it,
                                        title = movie[i].title,
                                        releaseDate = movie[i].releaseDate,
                                        sharedTransitionScope = sharedTransitionScope,
                                        animatedContentScope =animatedContentScope
                                    ) {
                                        val id = Gson().toJson(movie[i].id)
                                        navController.navigate("${Routes.DETAIL.name}?$MOVIE_KEY=$id")
                                    }
                                }
                        }
                    }

                }
                else -> {}
            }
        }
    }
}

@Composable
private fun SearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    onClearClick: () -> Unit = {},
    onSearch: () -> Unit = {},
    navController: NavHostController
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(true) {
        focusRequester.requestFocus()
    }

    Surface(shadowElevation = 3.dp) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 8.dp)
        ) {
            val style = TextStyle(
                color = Color.Gray,
                fontWeight = FontWeight.SemiBold
            )
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
            }
            TextField(
                value = value, onValueChange = onValueChange,
                placeholder = { Text(text = "Search Movies", style = style) },
                textStyle = style.copy(color = Color.Black),
                singleLine = true,
                keyboardActions = KeyboardActions(onSearch = { onSearch() }),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Search
                ),
                trailingIcon = {
                    if (value.isNotEmpty()) IconButton(onClick = onClearClick) {
                        Icon(Icons.Default.Clear, null)
                    }
                },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent
                ),
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequester),
            )
        }
    }
}