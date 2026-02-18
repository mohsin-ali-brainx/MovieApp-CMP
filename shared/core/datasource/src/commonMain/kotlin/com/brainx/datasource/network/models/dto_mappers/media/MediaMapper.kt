package com.brainx.datasource.network.models.dto_mappers.media

import com.brainx.datasource.network.models.response_models.media.MediaDataModel
import com.brainx.datasource.network.models.response_models.media.SearchMultiMovieResponse
import com.brainx.domain.network.models.media.MediaModel
import com.brainx.domain.network.models.media.SearchMultiMovieResponseModel

fun SearchMultiMovieResponse.toMapper() = SearchMultiMovieResponseModel(
    page = page,
    results = results.map { it.toMapper() },
    totalPages = totalPages,
    totalResults = totalResults
)

fun MediaDataModel.toMapper() = MediaModel(
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