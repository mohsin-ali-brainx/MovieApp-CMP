package com.brainx.movie_app.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import movieappcmp.composeapp.generated.resources.Res
import movieappcmp.composeapp.generated.resources.inter_tight_bold
import movieappcmp.composeapp.generated.resources.inter_tight_extra_bold
import movieappcmp.composeapp.generated.resources.inter_tight_light
import movieappcmp.composeapp.generated.resources.inter_tight_medium
import movieappcmp.composeapp.generated.resources.inter_tight_regular
import movieappcmp.composeapp.generated.resources.inter_tight_semi_bold
import movieappcmp.composeapp.generated.resources.inter_tight_thin
import movieappcmp.composeapp.generated.resources.ubuntu_bold
import movieappcmp.composeapp.generated.resources.ubuntu_bold_italic
import movieappcmp.composeapp.generated.resources.ubuntu_italic
import movieappcmp.composeapp.generated.resources.ubuntu_light
import movieappcmp.composeapp.generated.resources.ubuntu_light_italic
import movieappcmp.composeapp.generated.resources.ubuntu_medium
import movieappcmp.composeapp.generated.resources.ubuntu_medium_italic
import movieappcmp.composeapp.generated.resources.ubuntu_regular


@Composable
private fun InterFontFamily() = FontFamily(
    Font(Res.font.inter_tight_regular),
    Font(Res.font.inter_tight_bold),
    Font(Res.font.inter_tight_light),
    Font(Res.font.inter_tight_medium),
    Font(Res.font.inter_tight_semi_bold),
    Font(Res.font.inter_tight_extra_bold),
    Font(Res.font.inter_tight_thin),
    Font(Res.font.ubuntu_bold),
    Font(Res.font.ubuntu_bold_italic),
    Font(Res.font.ubuntu_italic),
    Font(Res.font.ubuntu_light),
    Font(Res.font.ubuntu_light_italic),
    Font(Res.font.ubuntu_medium),
    Font(Res.font.ubuntu_medium_italic),
    Font(Res.font.ubuntu_regular),
)

@Composable
fun appPrimaryFontFamily() = InterFontFamily()

@Composable
fun InterTypography() =
    Typography().run {
        val fontFamily = appPrimaryFontFamily()
        copy(
            headlineLarge = TextStyle(
                fontWeight = FontWeight.W500,
                fontFamily = fontFamily,
                fontSize = 38.sp,
            ),
            headlineMedium = TextStyle(
                fontWeight = FontWeight.W500,
                fontFamily = fontFamily,
                fontSize = 32.sp,
            ),
            headlineSmall = TextStyle(
                fontWeight = FontWeight.W500,
                fontFamily = fontFamily,
                fontSize = 26.sp,
            ),
            bodyLarge = TextStyle(
                fontWeight = FontWeight.W500,
                fontFamily = fontFamily,
                fontSize = 20.sp,
            ),
            bodyMedium = TextStyle(
                fontWeight = FontWeight.W400,
                fontFamily = fontFamily,
                fontSize = 18.sp,
            ),
            bodySmall = TextStyle(
                fontWeight = FontWeight.W400,
                fontFamily = fontFamily,
                fontSize = 16.sp,
            ),
            labelMedium = TextStyle(
                fontWeight = FontWeight.W400,
                fontFamily = fontFamily,
                fontSize = 14.sp,
            ),
            labelSmall = TextStyle(
                fontWeight = FontWeight.W400,
                fontFamily = fontFamily,
                fontSize = 12.sp,
            ),
        )
    }
