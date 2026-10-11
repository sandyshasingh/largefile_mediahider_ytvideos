package com.example.cleanfiles

import android.content.Context
import android.os.Build
import android.os.Environment
import android.os.StatFs
import androidx.annotation.RequiresApi


open class BaseConfig(val context: Context) {
    companion object {
        fun newInstance(context: Context) = BaseConfig(context)
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




}