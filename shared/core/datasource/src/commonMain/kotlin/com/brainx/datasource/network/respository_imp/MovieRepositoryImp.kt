package com.brainx.datasource.network.respository_imp

import com.brainx.datasource.network.ApiEndpoints
import com.brainx.datasource.network.mappers.toNetworkResultState
import com.brainx.datasource.network.models.dto_mappers.media.toMapper
import com.brainx.datasource.network.models.dto_mappers.video.toDomainModel
import com.brainx.datasource.network.models.response_models.media.SearchMultiMovieResponse
import com.brainx.datasource.network.models.response_models.video.VideoDataResponse
import com.brainx.domain.network.models.media.SearchMultiMovieResponseModel
import com.brainx.domain.network.models.video.VideoResponseModel
import com.brainx.domain.network.repository.MovieRepository
import com.brainx.domain.network.result_state.NetworkResultState
import com.brainx.ktor_network.core.safe_call.safeNetworkCall
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.appendPathSegments
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow

class MovieRepositoryImp(
    private val httpClient: HttpClient,
    private val apiKey:String,
) : MovieRepository {
    override fun search(
        page: Int,
        searchText: String
    ): Flow<NetworkResultState<SearchMultiMovieResponseModel>> = channelFlow {
        val result = safeNetworkCall<SearchMultiMovieResponse>(
            block = httpClient.get(urlString = ApiEndpoints.Videos.SearchMulti) {
                parameter("api_key", apiKey)
                parameter("query", searchText)
                parameter("page", page)

            }
        )

        send(
            result.toNetworkResultState { response ->
                response?.toMapper()
            }
        )

    }

    override fun getMovieVideoData(id: Int): Flow<NetworkResultState<VideoResponseModel>> = channelFlow  {
        val result = safeNetworkCall<VideoDataResponse>(
            block = httpClient.get(urlString = ApiEndpoints.Videos.MovieVideo) {
                url {
                    appendPathSegments("${id}")
                    appendPathSegments(ApiEndpoints.Videos.Videos)
                    parameters.append("api_key", apiKey)
                }
            }
        )

        send(
            result.toNetworkResultState { response ->
                response?.toDomainModel()
            }
        )
    }

    override fun getTvVideoData(id: Int): Flow<NetworkResultState<VideoResponseModel>> = channelFlow  {
        val result = safeNetworkCall<VideoDataResponse>(
            block = httpClient.get(urlString = ApiEndpoints.Videos.TvVideo) {
                url {
                    appendPathSegments("${id}")
                    appendPathSegments(ApiEndpoints.Videos.Videos)

                    parameters.append("api_key", apiKey)
                }
            }
        )

        send(
            result.toNetworkResultState { response ->
                response?.toDomainModel()
            }
        )
    }
}