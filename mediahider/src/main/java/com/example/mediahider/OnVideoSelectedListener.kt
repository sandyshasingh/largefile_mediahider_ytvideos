package com.example.mediahider

interface OnVideoSelectedListener {
    fun onVideoSelected(hiddenVideos: HiddenFiles)
    fun onVideoDeselected(hiddenVideos: HiddenFiles)
    fun onVideoClicked(hiddenVideos: List<HiddenFiles>,position:Int)
    fun onVideoFolderClicked(hiddenVideos: HiddenFiles)
}
