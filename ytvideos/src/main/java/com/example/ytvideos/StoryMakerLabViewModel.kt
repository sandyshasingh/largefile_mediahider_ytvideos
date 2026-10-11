package com.example.ytvideos

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StoryMakerLabViewModel(
//    private val useCase: GetCategoriesUseCase,
//    private val storyFrameUseCase: GetStoryFrameUseCase
) : ViewModel() {

//    private val _storyTemplateCategoriesDataState =
//        MutableStateFlow<TemplateCategoryUiState>(TemplateCategoryUiState.Loading)
//    val storyTemplateCategoriesDataState: StateFlow<TemplateCategoryUiState> =
//        _storyTemplateCategoriesDataState
//
//    fun loadStoryTemplateCategories() {
//        Log.d("categories", "loadCategories")
//        viewModelScope.launch(Dispatchers.IO) {
//            useCase()
//                .onStart {
//                    _storyTemplateCategoriesDataState.value = TemplateCategoryUiState.Loading
//                }
//                .catch {
//                    Log.d("categories", "catch")
//                    _storyTemplateCategoriesDataState.value =
//                        TemplateCategoryUiState.Error(it.message ?: "Error")
//                }
//                .collect { list ->
//                    Log.d("categories", "collect")
//                    _storyTemplateCategoriesDataState.value = TemplateCategoryUiState.Success(list)
//                }
//        }
//    }
//
//    // ✅ SOLUTION: हर category के लिए अलग StateFlow
//    private val categoryStateFlows = mutableMapOf<String, MutableStateFlow<StoryFrameUiState>>()
//
//    // Cache for each category
//    private val categoryCache = mutableMapOf<String, List<CategoryItems>>()

    /**
     * ✅ हर category के लिए उसका own StateFlow return करता है
     */
//    fun getCategoryStateFlow(categoryId: String): StateFlow<StoryFrameUiState> {
//        return categoryStateFlows.getOrPut(categoryId) {
//            MutableStateFlow(StoryFrameUiState.Loading)
//        }
//    }

    // Track which categories are currently being loaded
    private val loadingCategories = mutableSetOf<String>()

    fun loadCategory(categoryId: String, categoryName: String) {
        Log.d("loadCategory", "Loading categoryId: $categoryId, categoryName: $categoryName")

        // Get or create StateFlow for this category
//        val stateFlow = categoryStateFlows.getOrPut(categoryId) {
//            MutableStateFlow(StoryFrameUiState.Loading)
//        }
//
//        // Cache available → return instantly
//        if (categoryCache.containsKey(categoryId)) {
//            stateFlow.value = StoryFrameUiState.Success(categoryCache[categoryId]!!)
//            Log.d("loadCategory", "Loaded from cache: $categoryId")
//            return
//        }

        // Already loading? Skip duplicate request
        if (loadingCategories.contains(categoryId)) {
            Log.d("loadCategory", "Already loading: $categoryId")
            return
        }

        // Mark as loading
        loadingCategories.add(categoryId)

        viewModelScope.launch(Dispatchers.IO) {
//            storyFrameUseCase.getCategoryStoryFrames(categoryId, categoryName)
//                .onStart {
//                    stateFlow.value = StoryFrameUiState.Loading
//                }
//                .catch { e ->
//                    Log.e("loadCategory", "Error loading $categoryId: ${e.message}")
//                    stateFlow.value = StoryFrameUiState.Error(e.message ?: "Error")
//                    // Remove from loading set on error
//                    loadingCategories.remove(categoryId)
//                }
//                .collect { list ->
//                    val filtered = list.filter { it.categoryId == categoryId }
//
//                    Log.d("loadCategory", "Loaded categoryId: $categoryId, items: ${filtered.size}")
//
//                    // Update cache
//                    categoryCache[categoryId] = filtered
//
//                    // Update specific category's StateFlow
//                    stateFlow.value = StoryFrameUiState.Success(filtered)
//
//                    // Remove from loading set on success
//                    loadingCategories.remove(categoryId)
//                }
        }
    }

    /**
     * It fetches the data of trending story lab templates for Dashboard
     */
//    private val _trendingStoryTemplateDataState =
//        MutableStateFlow<StoryFrameUiState>(StoryFrameUiState.Loading)
//    val trendingStoryTemplateDataState = _trendingStoryTemplateDataState
//
//    fun getTrendingStoryLabTemplates(context: Context) {
//        if(!NetworkUtils.isNetworkAvailable(context)){
//            Log.d("isNetworkAvailable","1")
//            viewModelScope.launch(Dispatchers.IO) {
//                delay(2500)
//                withContext(Dispatchers.Main){
//                    _trendingStoryTemplateDataState.value =
//                        StoryFrameUiState.Error("")
//                }
//            }
//        }else {
//            Log.d("isNetworkAvailable","2")
//            viewModelScope.launch(Dispatchers.IO) {
//                storyFrameUseCase.getTrendingStoryLabTemplates()
//                    .onStart {
//                        _trendingStoryTemplateDataState.value = StoryFrameUiState.Loading
//                    }
//                    .catch { e ->
//                        _trendingStoryTemplateDataState.value =
//                            StoryFrameUiState.Error(e.message ?: "Error")
//                    }
//                    .collect { list ->
//                        _trendingStoryTemplateDataState.value = StoryFrameUiState.Success(list)
//                    }
//            }
//        }
//    }
}