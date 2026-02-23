package com.brainx.domain.network.dto_mappers.movie

import com.brainx.domain.network.models.media.MediaModel
import com.brainx.domain.network.models.media.SearchMultiMovieResponseModel
import kotlinx.serialization.Serializable

data class MovieTypeDTO(
    val mediaType: String,
    val mediaItems: List<MediaDTO>
)

@Serializable
data class MediaDTO(
    val id: Int? = null,
    val name: String? = null,
    val title: String? = null,
    val originalName: String? = null,
    val originalTitle: String? = null,
    val mediaType: String? = null,
    val adult: Boolean? = null,
    val backdropPath: String? = null,
    val posterPath: String? = null,
    val overview: String? = null,
    val originalLanguage: String? = null,
    val popularity: Double? = null,
    val releaseDate: String? = null,
    val firstAirDate: String? = null,
    val video: Boolean? = null,
    val voteAverage: Double? = null,
    val voteCount: Int? = null,
    val gender: Int? = null,
    val knownForDepartment: String? = null,
    val profilePath: String? = null,
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
                mediaItems = items
                    .sortedBy { item ->
                        item.title ?: item.originalTitle ?: item.name ?: item.originalName ?: ""
                    }
                    .map { it.toMediaDTO() }
            )
        }
}

fun MediaModel.toMediaDTO(): MediaDTO {
    return MediaDTO(
        id = id,
        name = name,
        title = title,
        originalName = originalName,
        originalTitle = originalTitle,
        mediaType = mediaType,
        adult = adult,
        backdropPath = backdropPath,
        posterPath = posterPath,
        overview = overview,
        originalLanguage = originalLanguage,
        popularity = popularity,
        releaseDate = releaseDate,
        firstAirDate = firstAirDate,
        video = video,
        voteAverage = voteAverage,
        voteCount = voteCount,
        gender = gender,
        knownForDepartment = knownForDepartment,
        profilePath = profilePath
    )
}
