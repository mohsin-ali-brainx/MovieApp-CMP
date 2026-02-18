package com.brainx.ktor_network.core.result_state
import com.brainx.ktor_network.core.error_handling.AppException
import io.ktor.http.Headers


sealed class ResultState<out T> {
    class Success<T>(val data: T?, val headers: Headers) : ResultState<T>()
    class Error(val error: AppException) : ResultState<Nothing>()
    class SuccessWithErrorData<T>(val data: T?, val error: AppException, val headers: Headers) : ResultState<T>()
}



/*
*
*
* Presentation -> Domain <- Datasource
*
*
* Datasource - >Ktor
* Datasource - >Room
* Datasource - >Local Pref
*
*
* BootStrap -> Datasource
*
* Presentation ->Bootstrap
*
* */