package com.example.largefiles

data class LargeItem(
    var path: String, var name: String = "", var size: Long = 0L, val mModified: Long = 0L,
    var isSelected: Boolean = false,
    var sizeInFormat: String
) {

}
