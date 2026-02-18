package com.brainx.domain.network.models.video


data class VideoResponseModel(
    val id: Int? = null,
    val results: ArrayList<VideoDataModel> = arrayListOf()
)