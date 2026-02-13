package com.brainx.ktor_network.core.safe_call

import com.brainx.ktor_network.core.error_handling.ExceptionHandle
import com.brainx.ktor_network.core.error_handling.ExceptionHandle.convertKtorErrorBody
import com.brainx.ktor_network.core.result_state.ResultState
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


suspend inline fun <reified D> safeNetworkCall(
    block: HttpResponse
): ResultState<D> {
    return try {
        if (block.status.isSuccess()) {
            ResultState.Success(block.body(), block.headers)
        } else {
            val error = block.convertKtorErrorBody()
            try {
                ResultState.SuccessWithErrorData(data = block.body(), headers = block.headers, error = error)
            } catch (e: Exception) {
                ResultState.Error(error)
            }
        }
    } catch (e: Exception) {
        ResultState.Error(ExceptionHandle.handleKtorException(e))
    }
}
