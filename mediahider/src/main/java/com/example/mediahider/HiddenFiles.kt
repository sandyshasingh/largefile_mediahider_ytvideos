package com.example.mediahider

import android.net.Uri
import java.io.Serializable

data class HiddenFiles(
    var path: String,
    var name: String? = null,
    var originalUri: Uri? = null,
    var size: String? = null,
    var type: String? = null,
    var updateTime: Long? = null,
    var isSelected: Boolean = false,
    var isFile: Boolean? = false,
    var count: Int? = 0
) : Serializable
