package com.brainx.movie_app.presentation.screens.main_home.ui_intents


import com.brainx.domain.network.dto_mappers.movie.MediaDTO
import kotlin.jvm.JvmInline


sealed interface MainHomeScreenUiIntents {
    sealed interface ButtonIntents{
        data object OnSearchButtonIntent : MainHomeScreenUiIntents
    }
    sealed interface TextFieldsIntent{
        @JvmInline
        value class OnSearchTextUpdate(val search:String) : MainHomeScreenUiIntents
    }
    sealed interface ListItemIntent{
        @JvmInline
        value class OnMovieItemClick(val media: MediaDTO) : MainHomeScreenUiIntents

        data object OnTriggerPagination : MainHomeScreenUiIntents
    }


}