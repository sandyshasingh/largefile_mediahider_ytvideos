package com.example.largefiles

import android.content.Context
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.provider.MediaStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class PhotoFetcher(
    var context: Context,
    var fetchPhotoAsyncCompleteListener: FetchPhotosAsyncCompleteListener,
    var isFolderData: Boolean
){



    fun execute() {
        var result :List<ListItem>? = null
        CoroutineScope(Dispatchers.IO).launch {
            result = getImages()
            CoroutineScope(Dispatchers.Main).launch{
                if (result!=null) {
                    fetchPhotoAsyncCompleteListener.fetchPhotosCompleted(result,isFolderData)
                }
            }
        }

    }

    private val IMAGE_PROJECTION = arrayOf(
        MediaStore.Images.ImageColumns._ID,
        MediaStore.Images.ImageColumns.WIDTH,
        MediaStore.Images.ImageColumns.HEIGHT,
        MediaStore.Images.ImageColumns.DISPLAY_NAME,
        MediaStore.Images.ImageColumns.DATE_TAKEN,
        MediaStore.Images.ImageColumns.DATE_MODIFIED,
        // MediaStore.Images.ImageColumns.MIME_TYPE,
        MediaStore.Images.ImageColumns.DATA,
        MediaStore.Images.ImageColumns.SIZE,
        // MediaStore.Images.ImageColumns.ORIENTATION
    )


    private fun getImages():ArrayList<ListItem>? {



        try {


            val data:ArrayList<ListItem> = java.util.ArrayList<ListItem>()

            val sortOrder = "${MediaStore.Video.Media.DATE_ADDED} DESC"
            val cursor = context?.contentResolver?.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION, null,
                null, sortOrder)!!
            cursor.use { cursor ->
                val imageData = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATA)
                val nameColNum = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DISPLAY_NAME)
                val dateTakenColNum = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATE_TAKEN)
                val dateModifiedColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATE_MODIFIED)

                val fileSize = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.SIZE)
                while (cursor.moveToNext()) {
                    val dateTaken = cursor.getLong(dateTakenColNum)
                    val name : String? = cursor.getString(nameColNum)
                    val uri = cursor.getString(imageData)
                    var imageWidth = cursor.getIntValue(MediaStore.Images.ImageColumns.WIDTH)

                    var imageHeight = cursor.getIntValue(MediaStore.Images.ImageColumns.HEIGHT)
                    val lastModify = cursor.getLong(dateModifiedColumnIndex)
                    val fileSizeValue = cursor.getLong(fileSize)
                    if (uri != null) {
                        val photo = ListItem(
                            uri,
                            name?:"",
                            fileSizeValue,
                            lastModify*1000,
                            null,
                            "",
                            "",
                            fileInfo = FileInfo(imageWidth, imageHeight, fileSizeValue,1),
                            isSelected = false
                        )
                        data.add(photo)
                    }
                }
            }
            return data


        } catch (e: OutOfMemoryError) {

            return null

        } catch (e: Exception) {
            return null
        }
    }

    interface FetchPhotosAsyncCompleteListener {
        fun fetchPhotosCompleted(photosList: List<ListItem>?,isFolderData: Boolean)
    }


}