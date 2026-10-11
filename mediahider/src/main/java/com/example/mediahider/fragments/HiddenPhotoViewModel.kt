package com.example.mediahider.fragments

import android.app.Application
import android.content.ContentUris
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mediahider.HiddenFiles
import com.example.mediahider.utils.MediaFileUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class HiddenFilesViewModel(application: Application) : AndroidViewModel(application), CoroutineScope by MainScope() {



    private val _mediaFiles = MutableLiveData<List<HiddenFiles>>()
    val mediaFiles: LiveData<List<HiddenFiles>> get() = _mediaFiles

    private val _videoFiles = MutableLiveData<List<HiddenFiles>>()
    val videoFiles: LiveData<List<HiddenFiles>> get() = _videoFiles

    fun loadFilesFromUri(folderUri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            val files = MediaFileUtils.getMediaFilesFromPath(getApplication(), folderUri)
            _mediaFiles.postValue(files)
        }
    }

    fun loadVideosFromUri(folderUri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            val files = MediaFileUtils.getVideoFilesFromPath(getApplication(), folderUri)
            _videoFiles.postValue(files)
        }
    }

}