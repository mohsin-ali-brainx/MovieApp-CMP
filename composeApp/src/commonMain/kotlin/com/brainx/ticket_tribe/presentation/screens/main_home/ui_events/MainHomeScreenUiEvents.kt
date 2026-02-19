package com.brainx.ticket_tribe.presentation.screens.main_home.ui_events

import com.brainx.domain.network.models.media.MediaModel

sealed interface MainHomeScreenUiEvents {
    sealed interface Navigate {
        data class MoveToDetail(val media: MediaModel) : MainHomeScreenUiEvents
    }

    sealed interface UserFeedback{
        data class ShowLoader(val isLoading: Boolean): MainHomeScreenUiEvents
    }

}