package com.brainx.domain.network.dto_mappers.movie

import com.brainx.domain.network.models.media.SearchMultiMovieResponseModel
import com.brainx.domain.network.models.meta_data.MetaData
import com.brainx.domain.network.models.meta_data.toMeta

data class SearchMultiMovieDto(
    val metaData: MetaData?,
    val result : List<MovieTypeDTO>
)

fun SearchMultiMovieResponseModel.toSearchMultiDTO(): SearchMultiMovieDto {
    return SearchMultiMovieDto(
        metaData = toMeta(),
        result = toMovieTypeDTO()
    )
}