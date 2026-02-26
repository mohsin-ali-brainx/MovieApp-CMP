package com.brainx.movie_app.presentation.screens.main_home.ui_state

import androidx.compose.runtime.Stable
import com.brainx.domain.network.dto_mappers.movie.SearchMultiMovieDto
import com.brainx.utils_extensions.constants.ExtConstants

@Stable
data class MainHomeScreenUiState(
    val searchText: String = ExtConstants.StringConstants.EMPTY,
    val searchResponse: SearchMultiMovieDto? = null,
    val isLoading: Boolean = false
)