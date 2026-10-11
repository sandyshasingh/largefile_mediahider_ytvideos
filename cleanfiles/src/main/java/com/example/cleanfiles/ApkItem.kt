package com.example.cleanfiles

data class ApkItem(
    var path: String, var name: String = "", var size: Long = 0L, val mModified: Long = 0L,
    var isSelected: Boolean = true,
    var sizeInFormat: String
)
