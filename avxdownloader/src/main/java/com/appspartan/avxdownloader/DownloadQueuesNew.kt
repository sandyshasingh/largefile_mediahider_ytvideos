package com.appspartan.avxdownloader

import android.content.Context
import android.util.Log
import com.appspartan.avxdownloader.utils.DownloadQueues
import com.appspartan.avxdownloader.utils.DownloadVideo
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable
import java.util.ArrayList

class DownloadQueuesNew : Serializable {
    private var downloads: MutableList<DownloadProgressVideo> = ArrayList<DownloadProgressVideo>()
    fun save(context: Context) {
        try {
            Log.d("dikshamyjaan","20 queue  ")

            val file = File(context.filesDir, "downloads.dat")
            val fileOutputStream = FileOutputStream(file)
            Log.d("dikshamyjaan","24 queue  ")

            val objectOutputStream = ObjectOutputStream(fileOutputStream)
            objectOutputStream.writeObject(this)
            objectOutputStream.close()
            fileOutputStream.close()
        } catch (e: IOException) {
            Log.d("dikshamyjaan","31 queue error ")

            e.printStackTrace()
        }
    }

    fun insertToTop(
        size: String,
        type: String,
        link: String,
        name: String,
        page: String,
        chunked: Boolean,
        website: String,
        bitmapPath: String,
        fileType: String,
        isPrivate: Boolean
    ) {

        val mName = getValidName(name, type)
        val video = DownloadProgressVideo()
        video.link = link
        video.name = mName
        video.page = page
        video.size = size
        video.type = type
        video.chunked = chunked
        video.website = website
        video.bitmapPath = bitmapPath
        video.fileType = fileType
        video.isPrivate = isPrivate
        downloads.add(video)
    }

    fun add(
        size: String,
        type: String,
        link: String,
        name: String,
        page: String,
        chunked: Boolean,
        website: String,
        bitmapPath: String,
        fileType: String
    ) {
        var name = name
        name = getValidName(name, type)
        val video = DownloadProgressVideo()
        video.link = link
        video.name = name
        video.page = page
        video.size = size
        video.type = type
        video.chunked = chunked
        video.website = website
        video.bitmapPath = bitmapPath
        video.fileType = fileType
        downloads.add(video)
    }

    private fun getValidName(name: String, type: String): String {
        var name = name
        name = name.replace("[^\\w ()'!\\[\\]\\-]".toRegex(), "")
        name = name.trim { it <= ' ' }
        if (name.length > 127) { //allowed filename length is 127
            name = name.substring(0, 127)
        } else if (name == "") {
            name = "video"
        }
        var i = 0
        var file: File = File(DownloadManager.getDownloadFolder(), "$name.$type")
        var nameBuilder = StringBuilder(name)
        while (file.exists()) {
            i++
            nameBuilder = StringBuilder(name)
            nameBuilder.append(" ").append(i)
            file = File(DownloadManager.getDownloadFolder(), "$nameBuilder.$type")
        }
        while (nameAlreadyExists(nameBuilder.toString())) {
            i++
            nameBuilder = StringBuilder(name)
            nameBuilder.append(" ").append(i)
        }
        return nameBuilder.toString()
    }

    val list: MutableList<DownloadProgressVideo>
        get() = downloads


    fun getTopVideo(): DownloadProgressVideo?{
        return if (downloads.size > 0) {
            downloads[0]
        } else {
            null
        }
    }

    fun deleteTopVideo(context: Context) {
        if (downloads.size > 0) {
            downloads.removeAt(0)
            save(context)
        }
    }

    private fun nameAlreadyExists(name: String): Boolean {
        for (video in downloads) {
            if (video.name.equals(name)) return true
        }
        return false
    }

    fun renameItem(index: Int, newName: String) {
        if (!downloads[index].name.equals(newName)) {
            downloads[index].name = downloads[index].type?.let { getValidName(newName, it) }
        }
    }

    companion object {
        fun load(context: Context): DownloadQueuesNew {
            val file = File(context.filesDir, "downloads.dat")
            var queues = DownloadQueuesNew()
            if (file.exists()) {
                try {
                    val fileInputStream = FileInputStream(file)
                    val objectInputStream = ObjectInputStream(fileInputStream)
                    queues = objectInputStream.readObject() as DownloadQueuesNew
                    objectInputStream.close()
                    fileInputStream.close()
                } catch (e: ClassNotFoundException) {
                    e.printStackTrace()
                    try {
                        val queuesOld: DownloadQueues = DownloadQueues.load(context)
                        val list: MutableList<DownloadProgressVideo> =
                            ArrayList<DownloadProgressVideo>()
                        if (queuesOld.getList().size > 0
                        ) {
                            for (item in queuesOld.getList()) {
                                val video: DownloadProgressVideo = getDownloadFile(item)
                                list.add(video)
                            }
                            val q = DownloadQueuesNew()
                            q.downloads = list
                            return q
                        }
                    } catch (exception: Exception) {
                        exception.printStackTrace()
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                    try {
                        val queuesOld: DownloadQueues = DownloadQueues.load(context)
                        val list: MutableList<DownloadProgressVideo> =
                            ArrayList<DownloadProgressVideo>()
                        if (queuesOld.getList().size > 0) {
                            for (item in queuesOld.getList()) {
                                val video: DownloadProgressVideo = getDownloadFile(item)
                                list.add(video)
                            }
                            val q = DownloadQueuesNew()
                            q.downloads = list
                            return q
                        }
                    } catch (exception: Exception) {
                        exception.printStackTrace()
                    }
                }
            }
            return queues
        }

        private fun getDownloadFile(prev_top_Video: DownloadVideo): DownloadProgressVideo {
            val downloadVideo = DownloadProgressVideo()
            downloadVideo.size = prev_top_Video.size
            downloadVideo.type = prev_top_Video.type
            downloadVideo.link = prev_top_Video.link
            downloadVideo.name = prev_top_Video.name
            downloadVideo.page = prev_top_Video.page
            downloadVideo.website = prev_top_Video.website
            downloadVideo.storagePath = prev_top_Video.storagePath
            downloadVideo.storagefilename = prev_top_Video.storagefilename
            downloadVideo.chunked = prev_top_Video.chunked
            downloadVideo.bitmapPath = prev_top_Video.bitmapPath
            downloadVideo.fileType = prev_top_Video.fileType
            downloadVideo.isPrivate = false
            return downloadVideo
        }
    }

}