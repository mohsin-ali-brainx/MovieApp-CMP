package com.brainx.domain.network.models.media

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


data class MediaModel(
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
