package com.example.largefiles

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.io.File
import java.util.Locale

class LargeImgVideoFetcher (
    var context: Context,
    var type: Int,
    var fetchDataAsyncCompleteListener: OnFetchDataAsyncCompleteListener,
) : CoroutineScope by MainScope() {

    var fileDataClassListe: ArrayList<LargeItem> = ArrayList()

    fun execute() {
        var result :ArrayList<LargeItem>? = null
        CoroutineScope(Dispatchers.IO).launch {
            result = fetchDataWithCursor()
            CoroutineScope(Dispatchers.Main).launch{
                if (result!=null) {
                    fetchDataAsyncCompleteListener.onFetchAppsCompleted(result,type)
                }
            }
        }

    }


    interface OnFetchDataAsyncCompleteListener {
        fun onFetchAppsCompleted(listItem: ArrayList<LargeItem>?, type: Int)
    }



    fun fetchDataWithCursor(): ArrayList<LargeItem> {

        try {
            val uri = MediaStore.Files.getContentUri("external")
            val projection = arrayOf(
                MediaStore.Files.FileColumns.MIME_TYPE,
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.DISPLAY_NAME,
                MediaStore.Files.FileColumns.SIZE,
                MediaStore.Files.FileColumns.DATE_MODIFIED,
                MediaStore.Files.FileColumns._ID
            )
            val fileDataClassList = java.util.ArrayList<LargeItem>()
            val collection = MediaStore.Files.getContentUri("external")


            queryCursor(uri, projection) { cursor ->

                try {
                    val fullMimetype = cursor.getStringValue(MediaStore.Files.FileColumns.MIME_TYPE)
                        ?.toLowerCase(Locale.getDefault()) ?: return@queryCursor


                    val size = cursor.getLongValue(MediaStore.Files.FileColumns.SIZE)
                    when(type){

                        VIDEOS_ID-> if (size <= 25 * 1024 * 1024) {
                            return@queryCursor
                        }

                        PHOTOS_ID->if (size <= 5 * 1024 * 1024) {
                            return@queryCursor
                        }
                    }


                    val path = cursor.getStringValue(MediaStore.Files.FileColumns.DATA)
                    val name : String = File(path).name?:""
                    val lastModified =
                        cursor.getLongValue(MediaStore.Files.FileColumns.DATE_MODIFIED)
                    val sizeFormat = size.let { format(it.toDouble(), 2) }
                    val mimetype = fullMimetype.substringBefore("/")

                    when(type){
                        VIDEOS_ID->{
                            if(mimetype == "video"){
                                val photo = LargeItem(
                                    path,
                                    name,
                                    size,
                                    lastModified,
                                    sizeInFormat = sizeFormat
                                )

                                fileDataClassList.add(photo)
                            }

                        }
                        PHOTOS_ID->{
                            if(mimetype == "image"){
                                val photo = LargeItem(
                                    path,
                                    name,
                                    size,
                                    lastModified,
                                    sizeInFormat = sizeFormat
                                )

                                fileDataClassList.add(photo)
                            }
                        }
                    }


                } catch (e: Exception) {
                }
            }
            return fileDataClassList;

        } catch (e: Exception) {
        }
        return fileDataClassListe

    }

    private fun queryCursor(uri: Uri?, projection: Array<String>, callback: (Cursor) -> Unit) {
        try {

            val cursor = uri?.let { context.contentResolver.query(it, projection, null, null, null) }
            cursor?.use {
                if (cursor.moveToFirst()) {
                    do {
                        callback(cursor)
                    } while (cursor.moveToNext())
                }
            }
        } catch (e: Exception) {
        }

    }

    fun format(bytes: Double, digits: Int): String {
        var byte = bytes
        val dictionary = arrayOf("bytes", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB")
        var index = 0
        while (index < dictionary.size) {
            if (byte < 1024) {
                break
            }
            byte /= 1024
            index++
        }
        return String.format("%." + digits + "f", byte) + " " + dictionary[index]
    }
}
