package com.example.mediahider.utils

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import com.example.mediahider.HiddenFiles
import com.example.mediahider.utils.StorageHelper.getFilePathFromUri

object MediaFileUtils {

    fun getMediaFilesFromPath(context: Context, folderUri: Uri): List<HiddenFiles> {
        val files = mutableListOf<HiddenFiles>()

        val rootDocFile = DocumentFile.fromTreeUri(context, folderUri)
        val photoFolder = rootDocFile?.findFile("AVX Photo Folder") ?: return files

        if (!photoFolder.isDirectory) return files

        photoFolder.listFiles().forEach { file ->
            if (file != null && file.isFile) {
                files.add(
                    HiddenFiles(

                        path = getFilePathFromUri(context,file.uri)?:"NA",
                        name = StorageUtils.newDecode(file.name) ?: "unknown",
                        size = StorageHelper.format(file.length().toDouble(), 1),
                        updateTime = file.lastModified(),
                        originalUri = file.uri,
                    )
                )
            }
        }

        // Sort by last modified, newest first
        return files.sortedByDescending { it.updateTime }
    }

    fun getVideoFilesFromPath(context: Context, folderUri: Uri): List<HiddenFiles> {
        val files = mutableListOf<HiddenFiles>()

        val rootDocFile = DocumentFile.fromTreeUri(context, folderUri)
        val photoFolder = rootDocFile?.findFile("AVX Video Folder") ?: return files

        if (!photoFolder.isDirectory) return files

        photoFolder.listFiles().forEach { file ->
            if (file != null && file.isFile) {
                files.add(
                    HiddenFiles(

                        path = getFilePathFromUri(context,file.uri)?:"NA",
                        name = StorageUtils.newDecode(file.name) ?: "unknown",
                        size = StorageHelper.format(file.length().toDouble(), 1),
                        updateTime = file.lastModified(),
                        originalUri = file.uri,
                    )
                )
            }
        }

        // Sort by last modified, newest first
        return files.sortedByDescending { it.updateTime }
    }
}