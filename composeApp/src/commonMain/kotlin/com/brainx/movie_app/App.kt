package com.brainx.movie_app

import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.brainx.movie_app.presentation.navigation.AppNavHostGraph
import com.brainx.movie_app.presentation.theme.MovieAppTheme


@Composable
@Preview
fun App() {

    MovieAppTheme {
        val navController = rememberNavController()

        Scaffold(
            Modifier.background(color = MaterialTheme.colorScheme.background)
        ) {
            AppNavHostGraph(navController = navController)
        }
    }
}