package com.brainx.ticket_tribe.presentation.theme

import androidx.compose.ui.graphics.Color

import androidx.compose.material3.*

object AppColors{
    val primaryColor = Color(0xFF228CC6)
    val secondaryColor = Color(0xFF2A4663)
    val tertiaryColor = Color(0x364081A5)

    val cardBackground = Color(0xFF26282E)
    val primaryWhiteTextColor = Color(0xFFFFFFFF)
    val secondaryTextColor = Color(0xFFC5C5C5)
    val mainBackgroundColor = Color(0XFF0D0B17)
    val primaryButtonColor = Color(0XFF228BC5)
    val mainEditTextColor = Color(0XFF2B2F39)

    val primaryWhiteIconColor = Color(0xFFFFFFFF)


}

val AppColorScheme = lightColorScheme(
    primary = AppColors.primaryColor,
    secondary = AppColors.secondaryColor,
    tertiary = AppColors.tertiaryColor,
    onPrimary = AppColors.primaryWhiteTextColor,
    onSecondary = AppColors.secondaryTextColor,
    primaryContainer = AppColors.cardBackground,
)

