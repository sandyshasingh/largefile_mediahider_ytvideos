package com.example.cleanfiles

import android.app.Application
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.AndroidViewModel

import androidx.lifecycle.MutableLiveData

class ApkViewModel(application: Application): AndroidViewModel(application),ApkFetcher.OnFetchDataAsyncCompleteListener
{

    var apps: MutableLiveData<ArrayList<ApkItem>> = MutableLiveData()

    fun fetchApks(context: Context){

        try {
            ApkFetcher(context, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)

        } catch (e:Exception){

        }

    }

    override fun onFetchAppsCompleted(
        dataList: ArrayList<ApkItem>?,
    ) {
        apps.value = dataList
    }
}
