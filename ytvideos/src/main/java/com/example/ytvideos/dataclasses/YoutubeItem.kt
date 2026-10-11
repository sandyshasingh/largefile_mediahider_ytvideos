package com.example.ytvideos.dataclasses

data class YoutubeItem(
    var title: String?,
    var categoryIcon: String?,
    var showViewAll: Boolean = false,
    val viewType:Int,
    val subCategories: List<ListItem> = emptyList()
)
