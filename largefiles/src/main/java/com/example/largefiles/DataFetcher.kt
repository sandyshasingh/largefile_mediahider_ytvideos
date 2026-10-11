package com.example.largefiles

import android.content.Context
import android.content.SharedPreferences
import android.database.Cursor
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.MediaStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.io.File
import java.util.ArrayList
import java.util.Locale

class DataFetcher(
    var context: Context,
    var fetchDataAsyncCompleteListener: OnFetchDataAsyncCompleteListener,
    var type: Int,

    ) : CoroutineScope by MainScope() {



    var fileDataClassListe: ArrayList<ListItem> = ArrayList()


    fun execute() {
        var result : ArrayList<ListItem>? =  null
        CoroutineScope(Dispatchers.IO).launch {
            result = fetchDataWithCursor()
            CoroutineScope(Dispatchers.Main).launch{
                fetchDataAsyncCompleteListener.onFetchDataCollected(result, type)
            }
        }
    }




    interface OnFetchDataAsyncCompleteListener {
        fun onFetchDataCollected(dataList: ArrayList<ListItem>?, type: Int)
    }


    val extraAudioMimeTypes = arrayListOf("application/ogg")
    val extraDocumentMimeTypes = arrayListOf(
        "application/pdf",
        "application/msword",
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
        "application/javascript"
    )

    val allfileTypes  = arrayListOf("application/ogg",
        "image/jpg",
        "image/jpeg",
        "image/png",
        "application/pdf",
        "application/msword",
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
        "application/javascript",
        "application/zip",
        "application/vnd.android.package-archive")

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
    val extraPdfMimeTypes = arrayListOf("application/pdf")
    val zipTypes = arrayListOf(
        "application/zip"
    )
    val appTypes = arrayListOf(
        "application/vnd.android.package-archive"
    )
    fun getVideoInfo(file: File, fileType: Int): FileInfo? {
        var fileInfo: FileInfo? = null
        try {
            val retriever = MediaMetadataRetriever()
            retriever.setDataSource(Uri.parse(file.path).toString())
            val durationStr =
                retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            val bitrate = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE)
            val width = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)
            val height = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)
            fileInfo = FileInfo(
                width!!.toInt(),
                height!!.toInt(),
                file.length(),
                durationStr!!.toLong(),
                bitrate!!.toLong(),
                fileType
            )
            retriever.release()
        } catch (e: java.lang.Exception) {
            fileInfo = FileInfo(0, 0, file.length(), 0, 0, fileType)
        }
        return fileInfo
    }

    fun getAudioInfo(file: File, fileType: Int): FileInfo? {
        var fileInfo: FileInfo? = null
        try {
            val retriever = MediaMetadataRetriever()
            retriever.setDataSource(Uri.parse(file.path).toString())
            val durationStr =
                retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            val bitrate = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE)
            fileInfo = FileInfo(
                0,
                0,
                file.length(),
                durationStr!!.toLong(),
                bitrate!!.toLong(),
                fileType
            )
            retriever.release()
        } catch (e: java.lang.Exception) {
            fileInfo = FileInfo(0, 0, file.length(), 0, 0, fileType)
        }
        return fileInfo
    }


    fun fetchDataWithCursor(): ArrayList<ListItem> {

        try {

            val uri = MediaStore.Files.getContentUri("external")
            val projection = if(isAndroid11orGreater) {
                arrayOf(
                    MediaStore.Files.FileColumns.MIME_TYPE,
                    MediaStore.Files.FileColumns.DATA,
                    MediaStore.Files.FileColumns.DISPLAY_NAME,
                    MediaStore.Files.FileColumns.SIZE,
                    MediaStore.Files.FileColumns.DATE_MODIFIED,
                    MediaStore.Files.FileColumns._ID,
                    MediaStore.Files.FileColumns.DURATION,
                    MediaStore.Files.FileColumns.BITRATE,
                    MediaStore.Files.FileColumns.HEIGHT,
                    MediaStore.Files.FileColumns.WIDTH
                )
            }
            else{
                arrayOf(
                    MediaStore.Files.FileColumns.MIME_TYPE,
                    MediaStore.Files.FileColumns.DATA,
                    MediaStore.Files.FileColumns.DISPLAY_NAME,
                    MediaStore.Files.FileColumns.SIZE,
                    MediaStore.Files.FileColumns.DATE_MODIFIED,
                    MediaStore.Files.FileColumns._ID,

                    MediaStore.Files.FileColumns.HEIGHT,
                    MediaStore.Files.FileColumns.WIDTH
                )
            }
            val fileDataClassList = java.util.ArrayList<ListItem>()

            queryCursor(uri, projection) { cursor ->

                try {
                    val fullMimetype = cursor.getStringValue(MediaStore.Files.FileColumns.MIME_TYPE)
                        .lowercase(Locale.getDefault()) ?: return@queryCursor
                    val path = cursor.getStringValue(MediaStore.Files.FileColumns.DATA)
//                    val name : String? = cursor.getStringValue(MediaStore.Files.FileColumns.DISPLAY_NAME)
                    val name : String? = File(path).name
                    if (name?.startsWith(".") == true) {
                        return@queryCursor
                    }

                    val size = cursor.getLongValue(MediaStore.Files.FileColumns.SIZE)
                    if (size == 0L) {
                        return@queryCursor
                    }
                    val lastModified =
                        cursor.getLongValue(MediaStore.Files.FileColumns.DATE_MODIFIED)

                    val mimetype = fullMimetype.substringBefore("/")

                    when (type) {

                        VIDEOS_ID -> {
                            val  id = cursor.getLongValue(MediaStore.Files.FileColumns._ID)
                            val imageUri =
                                Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + id)



                            val width = cursor.getLongValue(MediaStore.Files.FileColumns.WIDTH)
                            val height = cursor.getLongValue(MediaStore.Files.FileColumns.HEIGHT)
                            val bitrate = if (isAndroid11orGreater) {
                                cursor.getLongValue(MediaStore.Files.FileColumns.BITRATE)
                            } else 0
                            val durationStr = if (isAndroid11orGreater) {
                                cursor.getLongValue(MediaStore.Files.FileColumns.DURATION)
                            } else 0

                            if (mimetype == "video") {
                                fileDataClassList.add(
                                    ListItem(
                                        path,
                                        name?:"",
                                        size,
                                        lastModified * 1000,
                                        null,
                                        "",
                                        imageUri.toString(),
                                        FileInfo(
                                            width!!.toInt(),
                                            height!!.toInt(),
                                            size,
                                            durationStr!!.toLong(),
                                            bitrate!!.toLong(),
                                            2
                                        ),

                                    )
                                )
                            }

                        }

                        AUDIO_ID -> {
                            val width = cursor.getLongValue(MediaStore.Files.FileColumns.WIDTH)
                            val height = cursor.getLongValue(MediaStore.Files.FileColumns.HEIGHT)
                            val bitrate = if (isAndroid11orGreater) {
                                cursor.getLongValue(MediaStore.Files.FileColumns.BITRATE)
                            } else 0
                            val durationStr = if (isAndroid11orGreater) {
                                cursor.getLongValue(MediaStore.Files.FileColumns.DURATION)
                            } else 0
                            if (mimetype == "audio" || extraAudioMimeTypes.contains(fullMimetype)) {
                                fileDataClassList.add(
                                    ListItem(
                                        path,
                                        name?:"",

                                        size,
                                        lastModified * 1000,

                                        null,
                                        "",


                                        fileInfo = FileInfo(
                                            width!!.toInt(),
                                            height!!.toInt(),
                                            size,
                                            durationStr!!.toLong(),
                                            bitrate!!.toLong(),
                                            2
                                        ),
                                    )
                                )
                            }
                        }

                    }
                } catch (e: Exception) {

                }
            }
            return fileDataClassList;

        } catch (e: Exception) {
        }
        catch (error : OutOfMemoryError)
        { }
        return fileDataClassListe

    }

    private fun queryCursor(uri: Uri?, projection: Array<String>, callback: (Cursor) -> Unit) {
        try {
            val sortOrder = "${MediaStore.Video.Media.DATE_ADDED} DESC"
            val cursor = uri?.let { context.contentResolver.query(it, projection, null, null, sortOrder) }
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
