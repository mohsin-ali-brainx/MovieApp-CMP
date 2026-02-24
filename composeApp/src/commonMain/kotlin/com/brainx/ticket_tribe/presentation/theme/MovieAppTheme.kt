package com.brainx.ticket_tribe.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember

@Composable
fun MovieAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val appColorTheme = remember(darkTheme) { buildAppTheme(darkTheme) }

    CompositionLocalProvider(
        LocalAppTheme provides appColorTheme
    ){
        MaterialTheme(
            typography = InterTypography(),
            content = content
        )
    }
}