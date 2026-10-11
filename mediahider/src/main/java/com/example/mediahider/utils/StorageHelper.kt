package com.example.mediahider.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.storage.StorageManager
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.text.TextUtils
import android.util.Log
import androidx.annotation.RequiresApi



object StorageHelper {

    private const val DEVICE_DIR="Android/media"
    const val DEVICE_DIR_STATUS="com.whatsapp/WhatsApp/Media/.Statuses"
    const val DEVICE_DIR_LINKS="com.whatsapp/WhatsApp/Media/.Links"
    const val DEVICE_DIR_STATUS_BUSINESS="com.whatsapp.w4b/WhatsApp Business/Media/.Statuses"
    const val DOT_STATUSES=".Statuses"
    const val DOT_LINKS=".Links"





    @JvmStatic
    fun getPermission(context: Context,data: Uri?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (data != null) {
                context.contentResolver?.takePersistableUriPermission(data, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
        }
        saveWhatsappNewPath(data.toString(),context)
    }

    @JvmStatic
    fun getPermissionHider(context: Context,data: Uri?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (data != null) {
                context.contentResolver?.takePersistableUriPermission(data, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
        }
        AppThemePrefrences.SetSharedPreference(context, AppThemePrefrences.HIDER_URI, data.toString())
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




        @JvmStatic
        fun getWhatsappNewPath(context: Context): String? {
            return getSharedPrefStringValue(context, SAVE_ACTION_OPEN_DOCUMENT_TREE_PATH_WHATS_APP, "")
        }


        const val OPEN_DIRECTORY_REQUEST_CODE = 0xf11e

        private const val SAVE_ACTION_OPEN_DOCUMENT_TREE_PATH_WHATS_APP = "SAVE_ACTION_OPEN_DOCUMENT_TREE_PATH"

    @JvmStatic
    fun openDirectoryWhatsapp(context: Activity) {
         if (isR() && TextUtils.isEmpty(getWhatsappNewPath(context))) {
            try {
                val sm = context.getSystemService(Context.STORAGE_SERVICE) as StorageManager
                val intent = sm.primaryStorageVolume.createOpenDocumentTreeIntent()
                intent.putExtra("android.provider.extra.INITIAL_URI", getDocumentPath(context, DEVICE_DIR))
                context.startActivityForResult(intent, OPEN_DIRECTORY_REQUEST_CODE)
            } catch (e: Exception) {
            }
        }
    }

    @JvmStatic
    fun getFilePathFromUri(context: Context, uri: Uri): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA) // "_data" column
        context.contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            if (cursor.moveToFirst()) {
                Log.d("dikshamerijaan","82 ${cursor.getString(columnIndex)}")
                return cursor.getString(columnIndex)
            }
        }
        return null
    }


    @JvmStatic
    fun openDirectoryHider(context: Activity) {
        if (isR()) {
            try {
                val sm = context.getSystemService(Context.STORAGE_SERVICE) as StorageManager
                val intent = sm.primaryStorageVolume.createOpenDocumentTreeIntent()
                intent.putExtra("android.provider.extra.INITIAL_URI", getDocumentPath(context, "Documents"))
                context.startActivityForResult(intent, OPEN_DIRECTORY_REQUEST_CODE)
            } catch (e: java.lang.Exception) {
            }
        }
    }


    private fun saveWhatsappNewPath(path: String, context: Context) {
        setSharedPrefStringValue(
            context,
            SAVE_ACTION_OPEN_DOCUMENT_TREE_PATH_WHATS_APP,
            path
        )
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    @JvmStatic
    fun getDocumentPath(context: Context,startDir:String): Uri? {
        val dir=startDir.replace("/","%2F")
        val sm = context.getSystemService(Context.STORAGE_SERVICE) as StorageManager
        val intent = sm.primaryStorageVolume.createOpenDocumentTreeIntent()
        var uri = intent.getParcelableExtra<Uri>("android.provider.extra.INITIAL_URI")
        var scheme = uri.toString()
        scheme = scheme.replace("/root/", "/document/")
        scheme += "%3A$dir"
        uri = Uri.parse(scheme)
        return uri
    }

    @JvmStatic
    fun chekPersistPermissionFolder(uri: Uri?): Boolean {
        return if (uri!=null && !TextUtils.isEmpty(uri.path)){
            try {
                var path = uri.path
                if (path?.length!! >= 13) {
                    path = path.substring(path.length -13)
                }
                path.contains(DEVICE_DIR)
            }catch (e:Exception){
                return false
            }
        }else{
            false
        }
    }


    @JvmStatic
    fun chekPersistPermissionFolderHider(uri: Uri?): Boolean? {
        return if (uri!=null && !TextUtils.isEmpty(uri.path)){
            try {
                val path = uri.path

                if (path?.contains("Documents") == true && path.endsWith("Documents").not()){

                    return chekPersistPermissionFolderHiderVideoFolder(uri)

                }else{

                    return  path?.contains("Documents")

                }

            }catch (e:Exception){
                return false
            }
        }else{
            false
        }
    }
    @JvmStatic
    fun chekPersistPermissionFolderHiderVideoFolder(uri: Uri?): Boolean? {
        return if (uri!=null && !TextUtils.isEmpty(uri.path)){
            try {
                var path = uri.path
                path?.contains("Documents/Media Hider")
            }catch (e:Exception){
                return false
            }
        }else{
            false
        }
    }

    private fun setSharedPrefStringValue(context: Context, key: String?, value: String?) {
        val sharedPreferences = context.getSharedPreferences("com.mediahider", 0)
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
        editor.commit()
    }

    private fun getSharedPrefStringValue(context: Context, key: String?, defaultValue: String?): String? {
        val sharedPreferences = context.getSharedPreferences("com.mediahider", 0)
        return sharedPreferences.getString(key, defaultValue)
    }

    @JvmStatic
    fun isR(): Boolean {
        return SDK_INT >= Build.VERSION_CODES.R
    }



}