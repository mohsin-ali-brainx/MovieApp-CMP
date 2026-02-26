package com.brainx.movie_app.presentation.navigation

import kotlinx.serialization.Serializable
@Serializable
sealed class AppRoutes {
    @Serializable
    data object MainHome : AppRoutes()

    @Serializable
    data class Detail(val mediaDataModelJson: String) : AppRoutes()

    @Serializable
    data class VideoPlayer(
        val id:Int,
        val mediaType: String
    ) : AppRoutes()
}