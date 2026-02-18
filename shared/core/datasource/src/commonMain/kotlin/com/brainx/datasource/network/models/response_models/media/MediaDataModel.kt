package com.brainx.datasource.network.models.response_models.media

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
@Serializable
data class MediaDataModel(
    @SerialName("id") val id: Int? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("title") val title: String? = null,
    @SerialName("original_name") val originalName: String? = null,
    @SerialName("original_title") val originalTitle: String? = null,
    @SerialName("media_type") val mediaType: String? = null,
    @SerialName("adult") val adult: Boolean? = null,
    @SerialName("backdrop_path") val backdropPath: String? = null,
    @SerialName("poster_path") val posterPath: String? = null,
    @SerialName("overview") val overview: String? = null,
    @SerialName("original_language") val originalLanguage: String? = null,
    @SerialName("popularity") val popularity: Double? = null,
    @SerialName("release_date") val releaseDate: String? = null,
    @SerialName("first_air_date") val firstAirDate: String? = null,
    @SerialName("video") val video: Boolean? = null,
    @SerialName("vote_average") val voteAverage: Double? = null,
    @SerialName("vote_count") val voteCount: Int? = null,
    @SerialName("gender") val gender: Int? = null,
    @SerialName("known_for_department") val knownForDepartment: String? = null,
    @SerialName("profile_path") val profilePath: String? = null,
)
