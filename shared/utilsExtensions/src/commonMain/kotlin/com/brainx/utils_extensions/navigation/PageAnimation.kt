package com.brainx.utils_extensions.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import com.brainx.utils_extensions.constants.ExtConstants.AnimationsConstants.PAGE_ANIM_DURATION
import com.brainx.utils_extensions.utils.type_alias.SlideDirection
import kotlin.jvm.JvmSuppressWildcards
import kotlin.reflect.KType


inline fun <reified T : Any> NavGraphBuilder.horizontallyAnimatedComposable(
    tween: Int = PAGE_ANIM_DURATION,
    typeMap: Map<KType, @JvmSuppressWildcards NavType<*>> = emptyMap(),
    noinline content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit,
) {
    composable<T>(
        content = content,
        typeMap = typeMap,
        enterTransition = {
            slideIntoContainer(SlideDirection.Left, animationSpec = tween(tween))
        },
        exitTransition = {
            fadeOut(animationSpec = tween(tween))
        },
        popEnterTransition = {
            fadeIn(animationSpec = tween(tween))
        },
        popExitTransition = {
            slideOutOfContainer(SlideDirection.Right, animationSpec = tween(tween))
        }
    )
}