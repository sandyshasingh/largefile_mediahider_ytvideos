package com.example.cleanfiles

data class CleanmasterItems(
    var name: String = "",
    var size: Long = 0L,
    var list: ArrayList<ApkItem>?,
    var isFetched: Boolean = false,
    var allSelected: Boolean = true,
    var isDir: Boolean,
    var icon: Int
)
