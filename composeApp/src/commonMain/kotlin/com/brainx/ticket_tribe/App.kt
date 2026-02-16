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
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.brainx.datasource.local_pref.DatastorePreferenceManager
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource

import tickettribekmp.composeapp.generated.resources.Res
import tickettribekmp.composeapp.generated.resources.compose_multiplatform

import org.koin.compose.koinInject


@Composable
@Preview
fun App() {
    MaterialTheme {
        val pref = koinInject<DatastorePreferenceManager>()
        val scope = rememberCoroutineScope()

        var buttonText by remember { mutableStateOf("") }
        var showContent by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            buttonText = pref.getAccessToken().orEmpty()
        }

        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(
                onClick = {
                    scope.launch {
                        pref.setAccessToken("Token is Change")
                        buttonText = pref.getAccessToken().orEmpty()
                    }
                    showContent = !showContent
                }
            ) {
                Text(if (buttonText.isBlank()) "No Token" else buttonText)
            }

            AnimatedVisibility(showContent) {
                val greeting = remember { Greeting().greet() }
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Image(painterResource(Res.drawable.compose_multiplatform), null)
                    Text("Compose: $greeting")
                }
            }
        }
    }
}