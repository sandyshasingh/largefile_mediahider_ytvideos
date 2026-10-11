package com.example.ytvideos.retrofit

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ytvideos.HomeUiItem
import com.example.ytvideos.RemoteConfigUtils
import com.example.ytvideos.dataclasses.CategoriesList
import com.example.ytvideos.dataclasses.ListItem
import com.example.ytvideos.dataclasses.YoutubeItem
import com.example.ytvideos.dataclasses.YoutubeResponse
import com.example.ytvideos.dataclasses.YoutubeVideoItem
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import kotlinx.coroutines.launch
import java.util.Locale

class AuthViewModel (private val repository: ApiRepository) : ViewModel() {

    private val _homeUiItems = MutableLiveData<List<YoutubeItem>>()
    val homeUiItems: LiveData<List<YoutubeItem>> = _homeUiItems

    fun loadHomeData(categories:List<CategoriesList.Category>) {

        viewModelScope.launch {
            Log.d("diksha ki", categories.size.toString())

//            val categoryResponse: CategoriesList? =
//                RemoteConfigUtils.getCategoriesList()
//
//            val categories = categoryResponse?.data ?: emptyList()

            val youtubeResultsMap = mutableMapOf<String, List<YoutubeVideoItem>>()

            // Loop categories
            categories.forEach { category ->
//                Log.d("diko", category.toString())
                if (category == null) return@forEach
                if (!category.hasSubCategories) {

                    val query = category.categoryName ?: return@forEach

                    // YouTube API call
                    val result = repository.fetchYoutubeVideos(query)

                    if (result is ApiResult.Success) {
                        Log.d("paaroki","${result.data.items}")
                        youtubeResultsMap[query] = result.data.items
                    }
                    else if(result is ApiResult.Error){
                        Log.d("paaroki","${result}")
                    }
                }
            }

            // Build final UI list
            val finalUiList = buildHomeUiList(
                categories = categories,
                youtubeResultsMap = youtubeResultsMap
            )

            _homeUiItems.postValue(finalUiList)
        }
    }

    fun buildHomeUiList(
        categories: List<CategoriesList.Category?>,
        youtubeResultsMap: Map<String, List<YoutubeVideoItem>>
    ): List<YoutubeItem> {



//        val resultMap = LinkedHashMap<String, List<HomeUiItem>>()
        val resultMap = ArrayList<YoutubeItem>()

        val userCountry = getUserCountryCode()




        categories.forEach { category ->

            if (category == null) return@forEach

            val items = mutableListOf<ListItem>()


            if (category.hasSubCategories) {
                // CASE 1: Show sub categories
                category.subCategories.forEach { sub ->
                    if (userCountry == sub.country) {
                        items.add(
                            ListItem(
                                title = sub.subCategoryName ?: "",
                                thumbnail = sub.subCategoryImage ?: "",
                                url="",
                            )
                        )
                    // Show item
                    }

                }
            } else {
                // CASE 2: Show YouTube data
                val videos = youtubeResultsMap[category.categoryName] ?: emptyList()

                videos.forEach { video ->
                    Log.d("paaroki","$video")
                    items.add(
                        ListItem(
                            title = video.snippet.title,

                            thumbnail = video.snippet.thumbnails.medium.url,
                            url = video.id.videoId
                        )
                    )
                }
            }
            resultMap.add(YoutubeItem(category.categoryName.orEmpty(),category.categoryIcon,category.hasSubCategories,category.viewType,items))
//            if (items.isNotEmpty()) {
//                resultMap[categoryTitle] = items
//            }
        }
        return resultMap
    }

    private val _apiResponse = MutableLiveData<ApiResult<YoutubeResponse>>()
    val apiResponse: LiveData<ApiResult<YoutubeResponse>> get() = _apiResponse

//    fun sendOtp(email: String) {
//        viewModelScope.launch {
//            _apiResponse.value = ApiResult.Loading
//
//            val body = JsonObject().apply {
//                addProperty("email", email)
//            }
//
//            _apiResponse.value = repository.post("api/worker-management/send-otp", body)
//        }
//    }
//
//    fun getData(){
//        viewModelScope.launch {
//            _apiResponse.value = ApiResult.Loading
//
//            _apiResponse.value = repository.get("help-support")
//        }
//    }

    fun getUserCountryCode(): String {
        return Locale.getDefault().country  // e.g. "IN", "US"
    }

    fun getYoutubeData(category: String) {
        viewModelScope.launch {
            _apiResponse.value = ApiResult.Loading

            _apiResponse.value = repository.fetchYoutubeVideos(category)
        }
    }



}