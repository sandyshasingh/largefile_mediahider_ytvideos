package com.example.ytvideos.retrofit

import retrofit2.Response

sealed class ApiResult<out T> {
    data class Success<out T>(val data: T) : ApiResult<T>()
    data class Error(val message: String, val code: Int? = null) : ApiResult<Nothing>()
    object Loading : ApiResult<Nothing>()
}

suspend fun <T> safeApiCall(
    apiCall: suspend () -> Response<T>
): ApiResult<T> {
    return try {
        val response = apiCall()
        if (response.isSuccessful) {
            response.body()?.let { ApiResult.Success(it) }
                ?: ApiResult.Error("Empty response body", response.code())
        } else {
            ApiResult.Error(response.errorBody()?.string() ?: "Unknown error", response.code())
        }
    } catch (e: Exception) {
        ApiResult.Error(e.message ?: "Network call failed")
    }
}
