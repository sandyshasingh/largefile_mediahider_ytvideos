package com.example.ytvideos.retrofit

import com.example.ytvideos.dataclasses.YoutubeResponse
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.QueryMap
import retrofit2.http.Url

interface ApiService {
    @POST
    suspend fun postRequest(
        @Url url: String,
        @Body body: JsonObject
    ): Response<JsonElement>

    @GET
    suspend fun getRequest(
        @Url url: String,
        @QueryMap params: Map<String, String> = emptyMap()
    ): Response<JsonElement>


    @GET("search")
    suspend fun searchVideos(
        @Query("part") part: String = "snippet",
        @Query("q") query: String,                     // e.g., "podcast", "yoga"
        @Query("type") type: String = "video",
        @Query("maxResults") maxResults: Int = 20,
        @Query("regionCode") regionCode: String = "IN",
        @Query("key") apiKey: String
    ): Response<YoutubeResponse>

}