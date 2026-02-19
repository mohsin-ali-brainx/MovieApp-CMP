package com.brainx.ticket_tribe.presentation.screens.main_home.ui_state

import com.brainx.domain.network.dto_mappers.movie.SearchMultiMovieDto
import com.brainx.utils_extensions.constants.ExtConstants


data class MainHomeScreenUiState(
    val searchText:String= ExtConstants.StringConstants.EMPTY,
    val searchResponse: SearchMultiMovieDto?=null,
    val isLoading: Boolean=false
    )