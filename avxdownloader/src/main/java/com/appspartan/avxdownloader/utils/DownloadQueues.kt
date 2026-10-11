package com.appspartan.avxdownloader.utils

import android.content.Context
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable
import java.util.ArrayList

class DownloadQueues : Serializable {
    private val downloads: MutableList<DownloadVideo> = ArrayList<DownloadVideo>()
    fun save(context: Context) {
        try {
            val file = File(context.filesDir, "downloads.dat")
            val fileOutputStream = FileOutputStream(file)
            val objectOutputStream = ObjectOutputStream(fileOutputStream)
            objectOutputStream.writeObject(this)
            objectOutputStream.close()
            fileOutputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


    fun getList() : MutableList<DownloadVideo>{
        return downloads
    }

    companion object {
        fun load(context: Context): DownloadQueues {
            val file = File(context.filesDir, "downloads.dat")
            var queues = DownloadQueues()
            if (file.exists()) {
                try {
                    val fileInputStream = FileInputStream(file)
                    val objectInputStream = ObjectInputStream(fileInputStream)
                    queues = objectInputStream.readObject() as DownloadQueues
                    objectInputStream.close()
                    fileInputStream.close()
                } catch (e: ClassNotFoundException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            return queues
        }
    }
}