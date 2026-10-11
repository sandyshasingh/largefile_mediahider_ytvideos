package com.appspartan.avxdownloader

import java.io.Serializable

class DownloadProgressVideo : Serializable {
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
    var isPrivate = false
}
