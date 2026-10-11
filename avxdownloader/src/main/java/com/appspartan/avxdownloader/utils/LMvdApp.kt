package com.appspartan.avxdownloader.utils

import android.content.Intent
import com.appspartan.avxdownloader.DownloadManager

open class LMvdApp : BaseCoreApplication() {
    var downloadService: Intent? = null


    @JvmName("getDownloadService1")
    fun getDownloadService(): Intent? {
        return downloadService
    }


    override fun onCreate() {
        super.onCreate()
        instance = this
        downloadService = Intent(applicationContext, DownloadManager::class.java)
    }

    companion object {
        var instance: LMvdApp? = null

        @JvmName("getInstance1")
        fun getInstance(): LMvdApp? {
            return instance
        }
    }
}