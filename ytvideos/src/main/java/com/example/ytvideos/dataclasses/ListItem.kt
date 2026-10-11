package com.example.ytvideos.dataclasses

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ListItem(val title: String,
                    val thumbnail: String,
                    val url:String?) : Parcelable
