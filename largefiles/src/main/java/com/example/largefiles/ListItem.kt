package com.example.largefiles

import android.net.Uri

data class ListItem(
    var mPath: String,
    var mName: String,
    val mSize: Long? = 0L,
    val mModified: Long = 0L,
    val audioImageUri: Uri?,
    var mimeType: String?,
    var imageUri: String? = "",
    var fileInfo: FileInfo? = null,
    var isSelected: Boolean = false

    )

