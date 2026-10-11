package com.example.mediahider.asynctask

interface ProgressUpdateListener {
    fun onProgress(pos: Int,size:Int)
}