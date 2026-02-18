package com.brainx.datasource.network.mappers

import com.brainx.domain.helper_models.AppErrorException
import com.brainx.domain.helper_models.NetworkResponseHeaders
import com.brainx.domain.network.result_state.NetworkResultState
import com.brainx.ktor_network.core.error_handling.AppException
import com.brainx.ktor_network.core.result_state.ResultState
import io.ktor.http.Headers

inline fun <I, O> ResultState<I>.toNetworkResultState(
    crossinline dataMapper: (I?) -> O?
): NetworkResultState<O> {
    return when (this) {
        is ResultState.Success -> {
            NetworkResultState.Success(
                data = dataMapper(data),
                headers = headers.toDomainHeaders()
            )
        }

        is ResultState.Error -> {
            NetworkResultState.Error(error.toDomainError())
        }

        is ResultState.SuccessWithErrorData -> {
            NetworkResultState.SuccessWithErrorData(
                data = dataMapper(data),
                error = error.toDomainError(),
                headers = headers.toDomainHeaders()
            )
        }
    }
}

fun AppException.toDomainError(): AppErrorException = AppErrorException(
    errorMsg = errorMsg,
    errCode = errCode,
    errorLog = errorLog,
    isSessionExpired = isSessionExpired
)

fun Headers.toDomainHeaders(): NetworkResponseHeaders {
    val map = names().associateWith { key -> getAll(key).orEmpty() }
    return NetworkResponseHeaders(headers = map)
}