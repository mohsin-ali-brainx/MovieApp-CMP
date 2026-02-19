package com.brainx.ticket_tribe.presentation.screens.main_home.ui_intents


import com.brainx.domain.network.models.media.MediaModel
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
        value class OnMovieItemClick(val media: MediaModel) : MainHomeScreenUiIntents

        data object OnTriggerPagination : MainHomeScreenUiIntents
    }


}