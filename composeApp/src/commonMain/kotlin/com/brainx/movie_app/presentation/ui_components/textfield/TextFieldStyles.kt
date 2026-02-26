package com.brainx.movie_app.presentation.ui_components.textfield

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import com.brainx.movie_app.presentation.theme.LocalAppTheme
import com.brainx.movie_app.presentation.theme.appPrimaryFontFamily

@Composable
fun editTextStyle(
    fontSize: TextUnit = MaterialTheme.typography.bodyLarge.fontSize,
    textColor: Color = LocalAppTheme.current.primaryWhiteTextColor,
    textAlign: TextAlign = TextAlign.Start
)= TextStyle(
    textColor,
    fontWeight = FontWeight.Normal,
    fontSize = fontSize,
    fontFamily = appPrimaryFontFamily(),
    textAlign = textAlign)


