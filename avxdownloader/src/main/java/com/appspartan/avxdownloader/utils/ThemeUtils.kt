package com.appspartan.avxdownloader.utils

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Environment
import android.util.Log
import java.io.File

object ThemeUtils {

    const val PUBLIC_DIRECTORY_DOWNLODED = "AVXDownloads"

    fun getActivityIsAlive(activity: Activity?): Boolean {
        return if (SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            activity != null && !activity.isDestroyed
        } else {
            activity != null && !activity.isFinishing
        }

    }

    fun isServiceRunning(serviceClass: Class<*>, context: Context): Boolean {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        if (manager != null) {
            for (service in manager.getRunningServices(Int.MAX_VALUE)) {
                if (serviceClass.name == service.service.className) {
                    return true
                }
            }
        }
        return false
    }

    fun getPublicDownloadedVideoStorageDir(): File {
        // Get the directory for the app's private pictures directory.
        val file = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            PUBLIC_DIRECTORY_DOWNLODED
        )
        if (!file.mkdirs()) {
            Log.e("@STORAGE", "Directory not created")
        }
        return file
    }
}