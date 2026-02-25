package com.brainx.ticket_tribe

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.brainx.ticket_tribe.presentation.navigation.AppNavHostGraph
import com.brainx.ticket_tribe.presentation.theme.MovieAppTheme


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