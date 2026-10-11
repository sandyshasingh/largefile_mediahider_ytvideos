package com.example.ytvideos.listeners

interface ItemClickedListener {
    fun itmClicked(isPlay: Boolean, nameOrUrl: String, url: String)
}