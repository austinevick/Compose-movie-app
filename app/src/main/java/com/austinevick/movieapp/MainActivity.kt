package com.austinevick.movieapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.austinevick.movieapp.common.MOVIE_KEY
import com.austinevick.movieapp.theme.MovieAppTheme
import com.austinevick.movieapp.ui.favourites.FavouriteMoviesScreen
import com.austinevick.movieapp.ui.home.HomeScreen
import com.austinevick.movieapp.ui.moviedetail.MovieDetailScreen
import com.austinevick.movieapp.ui.search.SearchScreen
import com.austinevick.movieapp.ui.trailer.MovieTrailerScreen
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalSharedTransitionApi::class)
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = false
        }
        setContent {
            MovieAppTheme {
                SharedTransitionLayout {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Routes.HOME.name
                    ) {
                        composable(Routes.HOME.name) {
                            HomeScreen(
                                navController = navController,
                                sharedTransitionScope = this@SharedTransitionLayout,
                                animatedContentScope = this@composable
                            )
                        }
                        composable(
                            "${Routes.DETAIL.name}?$MOVIE_KEY={$MOVIE_KEY}",
                            arguments = listOf(
                                navArgument(name = MOVIE_KEY) {
                                    type = NavType.StringType
                                    defaultValue = ""
                                },
                            )
                        ) {
                            val movieJson = navController
                                .currentBackStackEntry?.arguments?.getString(MOVIE_KEY)
                            val movie = Gson().fromJson(movieJson, Int::class.java)
                            movie?.let {
                                MovieDetailScreen(
                                    navController, movie,
                                    sharedTransitionScope = this@SharedTransitionLayout,
                                    animatedContentScope = this@composable
                                )
                            }
                        }
                        composable(
                            "${Routes.TRAILER.name}?$MOVIE_KEY={$MOVIE_KEY}",
                            arguments = listOf(navArgument(name = MOVIE_KEY) {
                                type = NavType.StringType
                                defaultValue = ""
                            })
                        ) {
                            val movieJson =
                                navController.currentBackStackEntry?.arguments?.getString(MOVIE_KEY)
                            val movieId = Gson().fromJson(movieJson, String::class.java)
                            movieId?.let {
                                MovieTrailerScreen(navController = navController, it)

                            }
                        }
                        composable(Routes.FAVOURITE.name) {
                            FavouriteMoviesScreen(
                                navController = navController,
                                sharedTransitionScope = this@SharedTransitionLayout,
                                animatedContentScope = this@composable
                            )
                        }
                        composable(Routes.SEARCH.name) {
                            SearchScreen(
                                navController = navController,
                                sharedTransitionScope = this@SharedTransitionLayout,
                                animatedContentScope = this@composable

                            )
                        }
                    }

                }

            }
        }
    }
}

enum class Routes {
    HOME,
    DETAIL,
    TRAILER,
    FAVOURITE,
    SEARCH
}





