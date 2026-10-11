package com.example.ytvideos

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class HomeUiItem: Parcelable {

    @Parcelize
    data class YoutubeItem(
        val title: String,
        val thumbnail: String,
        val url:String?
    ) : HomeUiItem()

    @Parcelize
    data class SubCategoryItem(
        val name: String,
        val image: String
    ) : HomeUiItem()
}