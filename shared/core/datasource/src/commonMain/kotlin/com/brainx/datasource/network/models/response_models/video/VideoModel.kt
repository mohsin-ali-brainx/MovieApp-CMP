package com.brainx.datasource.network.models.response_models.video

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName


@Serializable
data class VideoModel(
    @SerialName("iso_639_1") val iso6391: String? = null,
    @SerialName("iso_3166_1") val iso31661: String? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("key") val key: String? = null,
    @SerialName("site") val site: String? = null,
    @SerialName("size") val size: Int? = null,
    @SerialName("type") val type: String? = null,
    @SerialName("official") val official: Boolean? = null,
    @SerialName("published_at") val publishedAt: String? = null,
    @SerialName("id") val id: String? = null

)