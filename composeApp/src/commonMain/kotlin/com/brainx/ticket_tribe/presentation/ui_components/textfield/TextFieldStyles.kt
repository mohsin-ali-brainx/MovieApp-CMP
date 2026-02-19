package com.brainx.ticket_tribe.presentation.ui_components.textfield

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import com.brainx.ticket_tribe.presentation.theme.AppColors
import com.brainx.ticket_tribe.presentation.theme.appPrimaryFontFamily

@Composable
fun EditTextStyle(
    fontSize: TextUnit = MaterialTheme.typography.bodyLarge.fontSize,
    textColor: Color = AppColors.primaryWhiteTextColor,
    textAlign: TextAlign = TextAlign.Start
)= TextStyle(
    textColor,
    fontWeight = FontWeight.Normal,
    fontSize = fontSize,
    fontFamily = appPrimaryFontFamily(),
    textAlign = textAlign)


