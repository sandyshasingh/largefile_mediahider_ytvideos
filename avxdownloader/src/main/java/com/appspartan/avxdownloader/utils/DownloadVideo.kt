package com.appspartan.avxdownloader.utils

import java.io.Serializable

class DownloadVideo : Serializable {
    var size: String? = null
    var type: String? = null
    var link: String? = null
    var name: String? = null
    var page: String? = null
    var website: String? = null
    var storagePath: String? = null
    var storagefilename: String? = null
    var chunked = false
    var bitmapPath: String? = null
    var fileType: String? = null
    var updateTime : Long? = null

    override fun hashCode(): Int {
        return storagePath.hashCode()
    }


    override fun equals(other: Any?): Boolean {
        val commonFile = other as DownloadVideo
        return storagePath.equals(commonFile.storagePath, ignoreCase = true)
    }
}