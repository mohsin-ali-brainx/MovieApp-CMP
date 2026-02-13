package com.brainx.ktor_network.core.error_handling

import com.brainx.ktor_network.core.models.ErrorResponseModel
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.parsing.ParseException
import io.ktor.util.network.UnresolvedAddressException

object ExceptionHandle {

    suspend fun handleKtorException(throwable: Throwable?): AppException {
        throwable?.let {
            return when (it) {
                is ResponseException ->{
                    return  convertKtorErrorBody(it)
                }
                is ParseException -> {
                    AppException(Errors.PARSE_ERROR, it)
                }
                is ConnectTimeoutException,is SocketTimeoutException -> {
                    AppException(Errors.TIMEOUT_ERROR, it)
                }
                is UnresolvedAddressException,is NoTransformationFoundException ->{
                    AppException(Errors.INTERNET_UNAVAILABLE, it)
                }
                else->{
                    AppException(error = it.message, errorLog = it.message)
                }
            }
        }
        return AppException(Errors.UNKNOWN,null)
    }

    suspend fun HttpResponse.convertKtorErrorBody(): AppException {
        if(bodyAsText().isNotBlank()){
            val errorResponse =  body<ErrorResponseModel>()
            return AppException(status.value, error = errorResponse.error)
        }else{
            return AppException(Errors.UNKNOWN,null)
        }
    }

    private suspend fun convertKtorErrorBody(exception: ResponseException): AppException {
        exception.apply {
            if (response.bodyAsText().isNotBlank()){
                response.apply {
                    val errorResponse =  body<ErrorResponseModel>()
                    return AppException(status.value, error = errorResponse.error)
                }
            }else{
                when(exception){
                    is RedirectResponseException ->{
                        response.status.apply {
                            return AppException(errCode = value,description)
                        }
                    }

                    is ClientRequestException ->{
                        response.status.apply {
                            return AppException(errCode = value,description)
                        }
                    }
                    is ServerResponseException ->{
                        response.status.apply {
                            return AppException(errCode = value,description)
                        }
                    }
                }
            }
        }
        return AppException(Errors.UNKNOWN,null)
    }
}