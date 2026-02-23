package com.brainx.ticket_tribe.presentation.screens.main_home.ui_events

import com.brainx.domain.network.dto_mappers.movie.MediaDTO

sealed interface MainHomeScreenUiEvents {
    sealed interface Navigate {
        data class MoveToDetail(val media: MediaDTO) : MainHomeScreenUiEvents
    }

    sealed interface UserFeedback{
        data class ShowLoader(val isLoading: Boolean): MainHomeScreenUiEvents
    }

}