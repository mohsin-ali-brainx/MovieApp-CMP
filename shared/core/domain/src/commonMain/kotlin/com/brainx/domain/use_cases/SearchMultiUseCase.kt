package com.brainx.domain.use_cases

import com.brainx.domain.network.dto_mappers.movie.SearchMultiMovieDto
import com.brainx.domain.network.dto_mappers.movie.toSearchMultiDTO
import com.brainx.domain.network.models.media.SearchMultiMovieResponseModel
import com.brainx.domain.network.repository.MovieRepository
import com.brainx.domain.network.result_state.NetworkResultState
import com.brainx.domain.utils.resource_state.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart

class SearchMultiUseCase(
    private val movieRepository: MovieRepository
) {
    operator fun invoke(searchText: String, page: Int): Flow<Resource<SearchMultiMovieDto>> {
        val result: Flow<NetworkResultState<SearchMultiMovieResponseModel>> =
            movieRepository.search(searchText = searchText, page = page)
        return result.map { resultState ->
            when (resultState) {
                is NetworkResultState.Success -> Resource.Success(resultState.data?.toSearchMultiDTO())
                is NetworkResultState.Error -> {
                    Resource.Error(resultState.error.errorMsg, null)
                }

                is NetworkResultState.SuccessWithErrorData -> Resource.Error(
                    resultState.error.errorMsg,
                    resultState.data?.toSearchMultiDTO()
                )
            }
        }.onStart {
            emit(Resource.Loading(true))
        }.catch {
            emit(Resource.Error(message = it.message ?: "Unknown Error"))
        }.onCompletion {
            emit(Resource.Loading(false))
        }
    }
}