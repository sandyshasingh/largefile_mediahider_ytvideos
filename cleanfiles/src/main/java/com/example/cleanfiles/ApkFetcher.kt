package com.example.cleanfiles

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.AsyncTask
import android.provider.MediaStore
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class ApkFetcher (
    var context: Context,
    var fetchDataAsyncCompleteListener: OnFetchDataAsyncCompleteListener,
    ) : AsyncTask<Void, Void, ArrayList<ApkItem>>() {

        var fileDataClassListe: ArrayList<ApkItem> = ArrayList()


        override fun doInBackground(vararg p0: Void?): ArrayList<ApkItem>? {

            return fetchDataWithCursor()
        }


        override fun onPostExecute(result: ArrayList<ApkItem>?) {
            super.onPostExecute(result)
            fetchDataAsyncCompleteListener.onFetchAppsCompleted(result)
        }



        interface OnFetchDataAsyncCompleteListener {
            fun onFetchAppsCompleted(audiosList: ArrayList<ApkItem>?)
        }

        val appTypes = arrayListOf(
            "application/vnd.android.package-archive"
        )

        fun fetchDataWithCursor(): ArrayList<ApkItem> {

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
                val fileDataClassList = java.util.ArrayList<ApkItem>()

                queryCursor(uri, projection) { cursor ->

                    try {
                        val fullMimetype = cursor.getStringValue(MediaStore.Files.FileColumns.MIME_TYPE)
                            ?.toLowerCase(Locale.getDefault()) ?: return@queryCursor


                        val size = cursor.getLongValue(MediaStore.Files.FileColumns.SIZE)
                        if (size == 0L) {
                            return@queryCursor
                        }


                        val path = cursor.getStringValue(MediaStore.Files.FileColumns.DATA)
                        val name : String = File(path).name?:""
                        val lastModified =
                            cursor.getLongValue(MediaStore.Files.FileColumns.DATE_MODIFIED)
                        val sizeFormat = size.let { format(it.toDouble(), 2) }


                        if(appTypes.contains(fullMimetype)){
                                    val photo = ApkItem(
                                        path,
                                        name,
                                        size,
                                        lastModified,
                                        sizeInFormat = sizeFormat
                                    )

                                    fileDataClassList.add(photo)
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
