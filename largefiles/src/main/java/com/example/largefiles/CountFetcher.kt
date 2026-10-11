package com.example.largefiles

import android.content.Context
import android.content.SharedPreferences
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class CountFetcher(var context: Context, var fetchDataAsyncCompleteListener: OnFetchDataAsyncCompleteListener, var type: Int) : CoroutineScope by MainScope() {



    var category_size :Long? = 0L
    var count:Int?=0


    fun execute() {
        var result : Int? = null
        launch {
            withContext(Dispatchers.IO){
                result =  fetchDataWithCursor()
                withContext(Dispatchers.Main){
                    fetchDataAsyncCompleteListener.onFetchCountCompleted(result, type, count, category_size)
                }
            }
        }
    }




    interface OnFetchDataAsyncCompleteListener {
        fun onFetchCountCompleted(audiosList: Int?, type:Int, count:Int?, categorySize:Long?)
    }


    val extraAudioMimeTypes = arrayListOf("application/ogg")
    val extraPdfMimeTypes = arrayListOf("application/pdf")
    val extraDocumentMimeTypes = arrayListOf(
        "application/pdf",
        "application/msword",
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
        "application/javascript"
    )

    val archiveMimeTypes = arrayListOf(
        "application/zip",
        "application/octet-stream",
        "application/json",
        "application/x-tar",
        "application/x-rar-compressed",
        "application/x-zip-compressed",
        "application/x-7z-compressed",
        "application/x-compressed",
        "application/x-gzip",
        "application/java-archive",
        "multipart/x-zip"
    )

    val zipTypes = arrayListOf(
        "application/zip"
    )


    fun fetchDataWithCursor(): Int? {

        try {
            val uri = MediaStore.Files.getContentUri("external")
            val projection = arrayOf(
                MediaStore.Files.FileColumns.MIME_TYPE,
//                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.DATE_MODIFIED,
                MediaStore.Files.FileColumns.DISPLAY_NAME,
                MediaStore.Files.FileColumns.SIZE
//                MediaStore.Files.FileColumns.DATE_MODIFIED
            )
//            val fileDataClassList = java.util.ArrayList<ListItem>()


            queryCursor(uri, projection) { cursor ->

                try {
                    val fullMimetype = cursor.getStringValue(MediaStore.Files.FileColumns.MIME_TYPE)
                        .lowercase(Locale.getDefault()) ?: return@queryCursor
                    val name = cursor.getStringValue(MediaStore.Files.FileColumns.DISPLAY_NAME)

                    if (name.startsWith(".")) {
                        return@queryCursor
                    }

                    val size = cursor.getLongValue(MediaStore.Files.FileColumns.SIZE)
                    when(type){

                        VIDEOS_ID-> if (size <= 25 * 1024 * 1024) {
                            return@queryCursor
                        }

                        PHOTOS_ID->if (size <= 5 * 1024 * 1024) {
                            return@queryCursor
                        }
                    }

                    val dateModified = cursor.getLongValue(MediaStore.Files.FileColumns.DATE_MODIFIED)
                    if (dateModified == 0L) {
                        return@queryCursor
                    }


                    val mimetype = fullMimetype.substringBefore("/")

                    when (type) {
                        PHOTOS_ID -> {
                            if (mimetype == "image") {
                                category_size = category_size?.plus(size)
                                count = count?.plus(1)
                            }
                        }

                        VIDEOS_ID -> {
                            if (mimetype == "video") {
                                category_size = category_size?.plus(size)
                                count = count?.plus(1)

                            }
                        }

                    }

                } catch (e: Exception) {
                }
            }
            return count?:0

        } catch (e: Exception) {

        }
        return null

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
}