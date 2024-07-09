package com.austinevick.movieapp.ui.trailer

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.austinevick.movieapp.composable.YoutubePlayer

@Composable
fun MovieTrailerScreen(
    navController: NavHostController,
    id: String,
    viewModel: MovieTrailerViewModel = hiltViewModel()
) {
    val uiState =
        viewModel.movieTrailerUIState
            .collectAsState(initial = MovieTrailerUIState.Loading)
    //val videoKey = viewModel.videoKey.collectAsState()
    val scope = rememberCoroutineScope()
    val key = remember {
        mutableStateOf("")
    }


    LaunchedEffect(key) {
        viewModel.getVideosByMovieId(id.toInt())
    }

    Scaffold(containerColor = Color.Black) {
        Column(modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(it)) {

            when (uiState.value) {
                is MovieTrailerUIState.Loading -> {
                    Box(
                        Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) { CircularProgressIndicator() }
                }

                is MovieTrailerUIState.Success -> {
                    val data = uiState.value as MovieTrailerUIState.Success

                    List(data.data.size) { i ->
                        Box(modifier = Modifier.padding(vertical = 12.dp)) {
                            Column {
                                YoutubePlayer(videoId = data.data[i].key)
                            Text(text = data.data[i].name,
                                fontSize = 18.sp, color = Color.White,
                                fontWeight = FontWeight.SemiBold)
                            }
                        }
                        
                    }
                }

                is MovieTrailerUIState.Error -> {
                    Box(
                        Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        TextButton(onClick = { /*TODO*/ }) {
                            Text(text = "Something went wrong")
                        }
                    }
                }
            }


        }
    }


}