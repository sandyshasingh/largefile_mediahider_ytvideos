package com.example.largefiles

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.AsyncTask
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.distinctUntilChanged
import com.example.largefiles.ThemeUtils.formatSize

class LargeFileViewModel(application: Application): AndroidViewModel(application),LargeImgVideoFetcher.OnFetchDataAsyncCompleteListener,
    PhotoFetcher.FetchPhotosAsyncCompleteListener,DataFetcher.OnFetchDataAsyncCompleteListener,
        CountFetcher.OnFetchDataAsyncCompleteListener
{

    var videoData: MutableLiveData<ArrayList<LargeItem>> = MutableLiveData()
    var imageData: MutableLiveData<ArrayList<LargeItem>> = MutableLiveData()

    val count_videos by lazy {  MutableLiveData<Int>()}
    val count_images by lazy {  MutableLiveData<Int>()}

    val size_photos by lazy {  MutableLiveData<String>()}
    val size_videos by lazy {  MutableLiveData<String>()}

    val photos by lazy {  MutableLiveData<List<ListItem>>()}
    val photos_update by lazy { photos.distinctUntilChanged() }

    val videos by lazy { MutableLiveData<List<ListItem>>() }
    val videos_update by lazy{ videos.distinctUntilChanged() }

    val audios by lazy { MutableLiveData<List<ListItem>>() }
    val audios_update by lazy{ audios.distinctUntilChanged() }

    fun fetchImages(context: Context){

        try {
            PhotoFetcher(context, this,false).execute()

        } catch (e:Exception){

        }

    }

    fun fetchVideos(context: Context){
        try{
            DataFetcher(context, this, VIDEOS_ID).execute()

        }  catch (e:Exception){}
    }

    fun fetchAudios(context: Context){
        try {
            DataFetcher(context, this, AUDIO_ID).execute()

        } catch (e:Exception){}
    }

    fun fetchLargeVideo(context: Context){

        try {
            LargeImgVideoFetcher(context, VIDEOS_ID,this).execute()

        } catch (e:Exception){

        }

    }
    fun fetchLargeImage(context: Context){

        try {
            LargeImgVideoFetcher(context, PHOTOS_ID,this).execute()

        } catch (e:Exception){

        }

    }

    fun fetchCount(context: Activity){
        CountFetcher(context,this, VIDEOS_ID).execute()
        CountFetcher(context,this, PHOTOS_ID).execute()

    }


    override fun onFetchAppsCompleted(listItem: ArrayList<LargeItem>?, type: Int) {
        when(type){
            VIDEOS_ID->{
                videoData.value = listItem
            }
            PHOTOS_ID->{
                imageData.value = listItem
            }
        }
    }

    override fun onFetchDataCollected(
        dataList: ArrayList<ListItem>?,
        type: Int
    ) {
         if(type == VIDEOS_ID){
             videos.postValue(dataList)
        }

        else if(type == AUDIO_ID){
            audios.postValue(dataList)
        }
    }

    override fun fetchPhotosCompleted(photosList: List<ListItem>?, isFolderData: Boolean) {
        photos.postValue(photosList)
    }

    override fun onFetchCountCompleted(
        audiosList: Int?,
        type: Int,
        count: Int?,
        categorySize: Long?
    ) {
        when (type) {
            PHOTOS_ID -> {
                count_images.postValue(count)
                if (categorySize != null) {

                    size_photos.postValue(formatSize(categorySize))
                }
            }
            VIDEOS_ID -> {
                count_videos.postValue(count)
                if (categorySize != null) {

                    size_videos.postValue(formatSize(categorySize))
                }
            }
        }
    }
}
