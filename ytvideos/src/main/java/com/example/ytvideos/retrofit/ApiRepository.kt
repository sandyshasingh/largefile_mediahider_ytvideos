package com.example.ytvideos.retrofit

import com.example.ytvideos.dataclasses.YoutubeResponse
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import retrofit2.Call

class ApiRepository(private val api: ApiService) {

    private val apiKey = "AIzaSyAljMxEpNdT5tUzh2Q2uWUAdPW4tRN1Ut0"

    suspend fun post(url: String, body: JsonObject): ApiResult<JsonElement> {
        return safeApiCall { api.postRequest(url, body) }
    }

    suspend fun get(url: String, params: Map<String, String> = emptyMap()): ApiResult<JsonElement> {
        return safeApiCall { api.getRequest(url, params) }
    }

    suspend fun fetchYoutubeVideos(category: String): ApiResult<YoutubeResponse> {
        return safeApiCall {
            api.searchVideos(
                query = category,
                apiKey = apiKey
            )
        }
    }

}