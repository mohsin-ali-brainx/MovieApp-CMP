package com.brainx.domain.network.models.media


data class SearchMultiMovieResponseModel(
    var page: Int? = null,
    var results: List<MediaModel> = listOf(),
    var totalPages: Int? = null,
    var totalResults: Int? = null
)