package com.appspartan.avxdownloader

import android.content.Context
import com.appspartan.avxdownloader.utils.DownloadVideo
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable
import java.util.ArrayList

class CompletedVideos : Serializable {
    val videos: MutableList<DownloadVideo> = ArrayList<DownloadVideo>()

    fun addVideo(context: Context, downloadVideo: DownloadVideo) {
        if (!videos.contains(downloadVideo)) {
            videos.add(0, downloadVideo)
        }
        save(context)
    }

    @JvmName("getVideos1")
    fun getVideos(): MutableList<DownloadVideo> {
        return videos
    }

    fun save(context: Context) {
        try {
            val file = File(context.filesDir, "completed.dat")
            val fileOutputStream = FileOutputStream(file)
            val objectOutputStream = ObjectOutputStream(fileOutputStream)
            objectOutputStream.writeObject(this)
            objectOutputStream.close()
            fileOutputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        fun load(context: Context?): CompletedVideos {
            var completedVideos = CompletedVideos()
            if (context != null) {
                val file = File(context.filesDir, "completed.dat")
                if (file.exists()) {
                    try {
                        val fileInputStream = FileInputStream(file)
                        val objectInputStream = ObjectInputStream(fileInputStream)
                        completedVideos = objectInputStream.readObject() as CompletedVideos
                        objectInputStream.close()
                        fileInputStream.close()
                    } catch (e: ClassNotFoundException) {
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
            return completedVideos
        }
    }
}