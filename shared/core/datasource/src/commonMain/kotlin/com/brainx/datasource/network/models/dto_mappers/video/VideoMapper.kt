package com.brainx.datasource.network.models.dto_mappers.video

import com.brainx.datasource.network.models.response_models.video.VideoDataResponse
import com.brainx.datasource.network.models.response_models.video.VideoModel
import com.brainx.domain.network.models.video.VideoDataModel
import com.brainx.domain.network.models.video.VideoResponseModel

fun VideoModel.toDomainModel(): VideoDataModel {
    return VideoDataModel(
        iso6391 = iso6391,
        iso31661 = iso31661,
        name = name,
        key = key,
        site = site,
        size = size,
        type = type,
        official = official,
        publishedAt = publishedAt,
        id = id
    )
}

fun VideoDataResponse.toDomainModel(): VideoResponseModel {
    return VideoResponseModel(
        id = id,
        results = ArrayList(results.map { it.toDomainModel() })
    )
}