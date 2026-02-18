package com.brainx.domain.network.models.meta_data

import com.brainx.domain.network.models.media.SearchMultiMovieResponseModel
import kotlinx.serialization.Serializable


data class MetaData(
    val page: Int? = null,
    val totalPages: Int? = null,
    val totalResults: Int? = null
)

fun SearchMultiMovieResponseModel.toMeta(): MetaData {
    return MetaData(
        page = page,
        totalPages=totalPages,
        totalResults=totalResults
    )
}
