package com.brainx.domain.network.dto_mappers.video

import com.brainx.domain.network.models.video.VideoDataModel

data class VideoDto(
    val id: String? = null,
    val name: String? = null,
    val site: String? = null,
    val type: String? = null,
    val key: String? = null,
)

fun VideoDataModel.toVideoDTO(): VideoDto {
    return VideoDto(
        id = id,
        name = name,
        site = site,
        type = type,
        key=key,
    )
}