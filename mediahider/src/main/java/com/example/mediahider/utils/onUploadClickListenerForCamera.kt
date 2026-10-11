package com.example.mediahider.utils

interface onUploadClickListener {
    fun onUploadPhotoClicked(path : String)
    fun onUploadVideoClicked(path : String)
    fun onUploadAudioClicked(path : String)
}