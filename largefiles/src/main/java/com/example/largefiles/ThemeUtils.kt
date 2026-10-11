package com.example.largefiles

import android.app.Activity
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Environment
import android.os.StatFs
import androidx.annotation.RequiresApi
import java.lang.StringBuilder

var isAndroid11orGreater = SDK_INT >= Build.VERSION_CODES.R


object ThemeUtils {

    fun getActivityIsAlive(activity: Activity?): Boolean {
        return if (SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            activity != null && !activity.isDestroyed
        } else {
            activity != null && !activity.isFinishing
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    fun getTotalInternalMemorySizeInLong(): Long {
        val path = Environment.getDataDirectory()
        val stat = StatFs(path.path)
        val blockSize = stat.blockSizeLong
        val totalBlocks = stat.blockCountLong
        return totalBlocks * blockSize
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    fun getAvailableInternalMemorySizeInLong(): Long {
        val path = Environment.getDataDirectory()
        val stat = StatFs(path.path)
        val blockSize = stat.blockSizeLong
        val availableBlocks = stat.availableBlocksLong
        return availableBlocks * blockSize
    }

    fun formatSize(size1: Long): String? {
        var suffix: String? = null
        var size = size1.toDouble()
        if (size >= 1024) {
            suffix = " KB"
            size /= 1024.0
            if (size >= 1024) {
                suffix = " MB"
                size /= 1024.0
                if (size >= 1024) {
                    suffix = " GB"
                    size /= 1024.0
                }
            }
        }
        size = round(size, 2)
        val resultBuffer = StringBuilder(size.toString())

        /*  int commaOffset = resultBuffer.length() - 3;
    while (commaOffset > 0) {
        resultBuffer.insert(commaOffset, ',');
        commaOffset -= 3;
    }*/if (suffix != null) resultBuffer.append(suffix)
        return resultBuffer.toString()
    }

    fun round(value: Double, places: Int): Double {
        var value = value
        require(places >= 0)
        val factor = Math.pow(10.0, places.toDouble()).toLong()
        value *= factor
        val tmp = Math.round(value)
        return tmp.toDouble() / factor
    }

}