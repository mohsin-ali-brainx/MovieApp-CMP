package com.brainx.ktor_network.core.result_state
sealed class Resource<out T>{
    class Success<out T>(val data: T?) : Resource<T>()
    class Error<out T>(val message: String, data: T? = null) : Resource<T>()
    class Loading(val isLoading: Boolean) : Resource<Nothing>()
}