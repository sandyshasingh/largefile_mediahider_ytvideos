package com.appspartan.avxdownloader.utils

import android.content.Context
import android.graphics.Bitmap
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

fun Bitmap?.createCacheFile(context: Context?): String? {
    var f: File?=null
    try {
        val bytes = ByteArrayOutputStream()
        this?.compress(Bitmap.CompressFormat.JPEG, 50, bytes)
        val  file= File(context?.cacheDir?.absolutePath+ "/.saveVideosFromInternetVp")
        if (!file.exists()){
            file.mkdir()
        }
        f = File(file.absolutePath + File.separator + System.currentTimeMillis() +".jpeg")
        f.createNewFile()
        val fo = FileOutputStream(f)
        fo.write(bytes.toByteArray())
        fo.close()
    }catch (e: IOException) {
    }catch (e1:Exception){

    }
    return f?.absolutePath
}