package com.brainx.datasource.network.models.response_models.media

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class SearchMultiMovieResponse(

    @SerialName("page") var page: Int? = null,
    @SerialName("results") var results: List<MediaDataModel> = listOf(),
    @SerialName("total_pages") var totalPages: Int? = null,
    @SerialName("total_results") var totalResults: Int? = null

)
