package com.brainx.domain.use_cases

import com.brainx.domain.network.dto_mappers.video.VideoDto
import com.brainx.domain.network.dto_mappers.video.toVideoDTO
import com.brainx.domain.network.repository.MovieRepository
import com.brainx.domain.network.result_state.NetworkResultState
import com.brainx.domain.utils.enums.MediaTypeEnums
import com.brainx.domain.utils.resource_state.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart

class GetVideoUseCase(
    private val movieRepository: MovieRepository
) {
    operator fun invoke(mediaType: String,id:Int): Flow<Resource<VideoDto>> {

        val response = if (mediaType== MediaTypeEnums.MOVIE.mediaType)
            movieRepository.getMovieVideoData(id = id)
        else movieRepository.getTvVideoData(id=id)

        return response.map { resultState ->
            when (resultState) {
                is NetworkResultState.Success -> {

                    if (resultState.data?.results?.isNotEmpty()==true){
                        Resource.Success(resultState.data.results.first().toVideoDTO())
                    }else{
                        Resource.Success(null)
                    }
                }
                is NetworkResultState.Error -> Resource.Error(resultState.error.errorMsg , null)
                is NetworkResultState.SuccessWithErrorData -> Resource.Error(resultState.error.errorMsg , resultState.data?.results?.first()?.toVideoDTO())
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