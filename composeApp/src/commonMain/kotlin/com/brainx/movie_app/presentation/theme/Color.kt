package com.brainx.movie_app.presentation.theme

import androidx.compose.ui.graphics.Color

import androidx.compose.material3.*
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf

@Immutable
interface AppColorTheme{
    val primaryColor : Color
    val secondaryColor : Color
    val tertiaryColor : Color

    val cardBackground: Color
    val primaryWhiteTextColor: Color
    val secondaryTextColor: Color
    val mainBackgroundColor: Color
    val primaryButtonColor : Color
    val mainEditTextColor : Color

    val primaryWhiteIconColor : Color
}

@Immutable
class AppLightThemeColor : AppColorTheme {
    override val primaryColor: Color
        get() = Color(0xFF228CC6)
    override val secondaryColor: Color
        get() = Color(0xFF2A4663)
    override val tertiaryColor: Color
        get() =  Color(0x364081A5)
    override val cardBackground: Color
        get() = Color(0xFF26282E)
    override val primaryWhiteTextColor: Color
        get() = Color(0xFFFFFFFF)
    override val secondaryTextColor: Color
        get() = Color(0xFFC5C5C5)
    override val mainBackgroundColor: Color
        get() = Color(0XFF0D0B17)
    override val primaryButtonColor: Color
        get() = Color(0XFF228BC5)
    override val mainEditTextColor: Color
        get() = Color(0XFF2B2F39)
    override val primaryWhiteIconColor: Color
        get() = Color(0xFFFFFFFF)

}

@Immutable
class AppDarkThemeColor : AppColorTheme {
    override val primaryColor: Color
        get() = Color(0xFF228CC6)
    override val secondaryColor: Color
        get() = Color(0xFF2A4663)
    override val tertiaryColor: Color
        get() =  Color(0x364081A5)
    override val cardBackground: Color
        get() = Color(0xFF26282E)
    override val primaryWhiteTextColor: Color
        get() = Color(0xFFFFFFFF)
    override val secondaryTextColor: Color
        get() = Color(0xFFC5C5C5)
    override val mainBackgroundColor: Color
        get() = Color(0XFF0D0B17)
    override val primaryButtonColor: Color
        get() = Color(0XFF228BC5)
    override val mainEditTextColor: Color
        get() = Color(0XFF2B2F39)
    override val primaryWhiteIconColor: Color
        get() = Color(0xFFFFFFFF)

}

internal fun buildAppTheme(isDark: Boolean): AppColorTheme {
    return if (isDark) {
        AppDarkThemeColor()
    } else {
        AppLightThemeColor()
    }
}

val LocalAppTheme = staticCompositionLocalOf<AppColorTheme> {
    error("LocalAppTheme not provided. Wrap your UI in AppTheme().")
}

val AppColorScheme = lightColorScheme(

)

