package com.brainx.domain.network.dto_mappers.movie

import com.brainx.domain.network.models.media.MediaModel
import com.brainx.domain.network.models.media.SearchMultiMovieResponseModel

data class MovieTypeDTO(
    val mediaType: String,
    val mediaItems: List<MediaModel>
)

fun SearchMultiMovieResponseModel.toMovieTypeDTO(): List<MovieTypeDTO> {

    return results
        .filter {
            it.id != null && !it.mediaType.isNullOrBlank()
        }
        .distinctBy {
            Pair(
                (it.id),
                it.mediaType?.trim()
            )
        }
        .groupBy { it.mediaType }
        .map { (mediaType, items) ->
            MovieTypeDTO(
                mediaType = mediaType ?: "unknown",
                mediaItems = items.sortedBy { item ->
                    item.title ?: item.originalTitle ?: item.name ?: item.originalName ?: ""
                }
            )
        }
}
