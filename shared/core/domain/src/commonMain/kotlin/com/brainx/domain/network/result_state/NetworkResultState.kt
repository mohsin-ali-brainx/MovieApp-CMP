package com.brainx.domain.network.result_state

import com.brainx.domain.helper_models.AppErrorException
import com.brainx.domain.helper_models.NetworkResponseHeaders

sealed class NetworkResultState<out T> {
    class Success<T>(val data: T?, val headers: NetworkResponseHeaders) : NetworkResultState<T>()
    class Error(val error: AppErrorException) : NetworkResultState<Nothing>()
    class SuccessWithErrorData<T>(val data: T?, val error: AppErrorException, val headers: NetworkResponseHeaders) : NetworkResultState<T>()
}