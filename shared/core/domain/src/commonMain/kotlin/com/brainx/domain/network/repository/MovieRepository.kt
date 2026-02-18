package com.brainx.domain.network.repository

import com.brainx.domain.network.models.media.SearchMultiMovieResponseModel
import com.brainx.domain.network.models.video.VideoResponseModel
import com.brainx.domain.network.result_state.NetworkResultState
import com.brainx.utils_extensions.constants.ExtConstants
import kotlinx.coroutines.flow.Flow

interface MovieRepository{
    fun search(page:Int= ExtConstants.IntegerConstants.ONE, searchText: String): Flow<NetworkResultState<SearchMultiMovieResponseModel>>
    fun getMovieVideoData(id: Int): Flow<NetworkResultState<VideoResponseModel>>
    fun getTvVideoData(id: Int): Flow<NetworkResultState<VideoResponseModel>>
}