package com.appspartan.avxdownloader

import android.app.IntentService
import android.content.Intent
import android.os.Environment
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.appspartan.avxdownloader.utils.DownloadFailException
import com.appspartan.avxdownloader.utils.LMvdApp
import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.io.StringWriter
import java.net.URL
import com.appspartan.avxdownloader.CompletedVideos.Companion.load

import java.net.URLConnection
import com.appspartan.avxdownloader.utils.DownloadVideo
import com.appspartan.avxdownloader.utils.ThemeUtils
import java.nio.ByteBuffer
import java.nio.channels.Channels

const val REMAINING_TASK_EXTRA = "REMAINING_TASK_EXTRA"
const val DOWNLOAD_FINISH_ACTION = "DOWNLOAD_FINISH_ACTION"
class DownloadManager : IntentService("DownloadManager") {

    private val mBytes: Long = 4096
    var PREV_LINK = ""

    override fun onHandleIntent(intent: Intent?) {

        stop = false
        downloadThread = Thread.currentThread()
        downloadNotifier =
            intent?.let { DownloadNotifier(it) }
        if (intent != null) {
            chunked = intent.getBooleanExtra("chunked", false)
            if (chunked) {
                downloadFile = null
                prevDownloaded = 0
                downloadSpeed = 0
                totalSize = 0
                handleChunkedDownload(intent)
            } else {
                prevDownloaded = 0
                val connection: URLConnection?
                try {
                    val size = intent.getStringExtra("size")
                    if (!TextUtils.isEmpty(size)) {
                        totalSize = size!!.toLong()
                    }
                    connection = URL(intent.getStringExtra("link")).openConnection()
                    val filename =
                        intent.getStringExtra("name") + "." + intent.getStringExtra("type")
                    val directory = prepareTargetDirectory()
                    if (!directory.absolutePath.endsWith("/")) {
                        downloadFolder = directory.absolutePath + "/"
                    } else {
                        downloadFolder = directory.absolutePath
                    }
                    val directotryExists = directory.exists() || directory.mkdir() || directory
                        .createNewFile()



                    if (directotryExists) {
                        downloadNotifier!!.notifyDownloading()
                        downloadFile = File(directory, filename)
                        if (connection != null) {
                            var out: FileOutputStream? = null
                            if (downloadFile != null && downloadFile!!.exists()) {
                                prevDownloaded = downloadFile!!.length()
                                //                                connection.setRequestProperty("Range", "bytes=" + downloadFile.length() + "-");
                                connection.connect()
                                out = FileOutputStream(downloadFile!!.absolutePath, true)
                            } else {
                                connection.connect()
                                if (downloadFile != null && downloadFile!!.createNewFile()) {
                                    out = FileOutputStream(downloadFile!!.absolutePath, true)
                                }
                            }
                            if (out != null && downloadFile != null && downloadFile!!.exists()) {
                                val inp = connection.getInputStream()
                                val readableByteChannel = Channels.newChannel(inp)
                                val fileChannel = out.channel
                                var downloadingLength = downloadFile!!.length()
                                while (downloadingLength < totalSize) {
                                    if (stop) return
                                    fileChannel.transferFrom(readableByteChannel, 0, 1024)
                                    downloadingLength = downloadFile!!.length()
                                }
                                readableByteChannel.close()
                                inp.close()
                                out.flush()
                                out.close()
                                fileChannel.close()
                                //writableByteChannel.close();
                                val mediaScanner = MediaScanner(
                                    LMvdApp.getInstance()!!.applicationContext
                                )
                                if (downloadFile != null) {
                                    mediaScanner.scan(downloadFile!!.path)
                                }
                                downloadFinished(filename, downloadFile)

                            }
                        }
                    } else {
                        onDownloadFailExceptionListener!!.onDownloadFailException("Can't create Download directory.")
                    }
                }
                catch (e: FileNotFoundException) {
                    if (onDownloadFailExceptionListener != null) {
                        onDownloadFailExceptionListener!!.onDownloadFailException(e.toString())
//                        if (BuildConfig.DEBUG) {
//                            Toasty.error(applicationContext, " Failed Downloading $e").show()
//                        }
                    }
                    // linkNotFound(intent);
                } catch (e: IOException) {
                    if (onDownloadFailExceptionListener != null) {
                        onDownloadFailExceptionListener!!.onDownloadFailException(e.toString())
//                        if (BuildConfig.DEBUG) {
//                            Toasty.error(applicationContext, " Failed Downloading $e").show()
//                        }
                    }
                }
                catch (e: DownloadFailException) {

                    if (onDownloadFailExceptionListener != null) {
                        onDownloadFailExceptionListener!!.onDownloadFailException(e.toString())
                    }
//                    if (BuildConfig.DEBUG) {
//                        Toasty.error(applicationContext, " Failed Downloading $e")
//                            .show()
//                    }
                } catch (e: Exception) {
                    onDownloadFailExceptionListener?.onDownloadFailException(e.toString())
//                    if (BuildConfig.DEBUG) {
//                        Toasty.error(applicationContext, " Failed Downloading $e").show()
//                    }
                }
            }
        }
    }

    private fun downloadFinished(filename: String, downloadedFile: File?) {
        /* if (downloadedFile != null) {
             sendDOwnloadedFileBroadCast(downloadedFile.path)
         }*/
        downloadNotifier?.notifyDownloadFinished()
        val fp: String?
        val queues: DownloadQueuesNew = DownloadQueuesNew.load(applicationContext)
        val completedVideos = load(applicationContext)
        val prev_top_Video: DownloadProgressVideo? = queues.getTopVideo()
        if(prev_top_Video != null)
        {
            prev_top_Video.storagePath = downloadedFile!!.path
            prev_top_Video.storagefilename = filename
            val video = getDownloadFile(prev_top_Video)
            completedVideos.addVideo(applicationContext, video)
            completedVideos.save(applicationContext)
        }
        queues.deleteTopVideo(applicationContext)
        onDownloadFinishedListener?.onDownloadFinished(downloadedFile)

        val intent = Intent(DOWNLOAD_FINISH_ACTION)
        intent.putExtra(REMAINING_TASK_EXTRA,queues.list.size)
        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)

        val topVideo: DownloadProgressVideo? = queues.getTopVideo()
        if (topVideo != null) {
            val downloadService: Intent? = LMvdApp.getInstance()?.getDownloadService()
            downloadService?.putExtra("link", topVideo.link)
            downloadService?.putExtra("name", topVideo.name)
            downloadService?.putExtra("type", topVideo.type)
            downloadService?.putExtra("size", topVideo.size)
            downloadService?.putExtra("page", topVideo.page)
            downloadService?.putExtra("chunked", topVideo.chunked)
            downloadService?.putExtra("website", topVideo.website)
            onHandleIntent(downloadService)
        }
        fp = prev_top_Video?.bitmapPath


        if (!TextUtils.isEmpty(fp)) {
            val file = File(fp)
            if (file.exists()) {
                if (file.delete()) {
                }
            }
        }
    }


    private fun getDownloadFile(prev_top_Video: DownloadProgressVideo): DownloadVideo {
        val downloadVideo = DownloadVideo()
        downloadVideo.size = prev_top_Video.size
        downloadVideo.type = prev_top_Video.type
        downloadVideo.link = prev_top_Video.link
        downloadVideo.name = prev_top_Video.name
        downloadVideo.page = prev_top_Video.page
        downloadVideo.website = prev_top_Video.website
        downloadVideo.storagePath = prev_top_Video.storagePath
        downloadVideo.storagefilename = prev_top_Video.storagefilename
        downloadVideo.chunked = prev_top_Video.chunked
        downloadVideo.bitmapPath = prev_top_Video.bitmapPath
        downloadVideo.fileType = prev_top_Video.fileType
        downloadVideo.updateTime = System.currentTimeMillis()
        return downloadVideo
    }

    private fun saveinDB(rowId: Long, oldFilePath: String, newFilePath: String) {
        /* val dbDao: FilepathDatabaseDao = MyApplication.getDaoSession().getFilepathDatabaseDao()
         val filepathDatabase = FilepathDatabase(rowId, oldFilePath, newFilePath)
         dbDao.insertOrReplace(filepathDatabase)*/
    }

    /*
        private fun linkNotFound(intent: Intent) {
            downloadNotifier!!.cancel()
            if (onLinkNotFoundListener != null) {
                onLinkNotFoundListener!!.onLinkNotFound()
            } else {
                val queues: DownloadQueuesNew = DownloadQueuesNew.load(applicationContext)
                queues.deleteTopVideo(applicationContext)
                val inactiveDownload = DownloadVideo()
                inactiveDownload.name = intent.getStringExtra("name")
                inactiveDownload.link = intent.getStringExtra("link")
                inactiveDownload.type = intent.getStringExtra("type")
                inactiveDownload.size = intent.getStringExtra("size")
                inactiveDownload.page = intent.getStringExtra("page")
                inactiveDownload.website = intent.getStringExtra("website")
                inactiveDownload.chunked = intent.getBooleanExtra("chunked", false)
                val inactiveDownloads: InactiveDownloads = InactiveDownloads.load(applicationContext)
                inactiveDownloads.add(applicationContext, inactiveDownload)
                val topVideo: DownloadProgressVideo = queues.getTopVideo()
                if (topVideo != null) {
                    val downloadService: Intent = LMvdApp.getInstance().getDownloadService()
                    downloadService.putExtra("link", topVideo.link)
                    downloadService.putExtra("name", topVideo.name)
                    downloadService.putExtra("type", topVideo.type)
                    downloadService.putExtra("size", topVideo.size)
                    downloadService.putExtra("page", topVideo.page)
                    downloadService.putExtra("chunked", topVideo.chunked)
                    downloadService.putExtra("website", topVideo.website)
                    onHandleIntent(downloadService)
                }
            }
        }
    */

    private fun handleChunkedDownload(intent: Intent) {
        try {
            val name = intent.getStringExtra("name")
            val type = intent.getStringExtra("type")
            val directory = prepareTargetDirectory()
                ?: throw DownloadFailException(
                    "File returned by prepareTargetDirectory() is " +
                            "null... IMPOSSIBLE!"
                )
            if (!directory.absolutePath.endsWith("/")) {
                downloadFolder = directory.absolutePath + "/"
            } else {
                downloadFolder = directory.absolutePath
            }
            val directotryExists = directory.exists() || directory.mkdir() || directory
                .createNewFile()
            if (directotryExists) {
                downloadNotifier!!.notifyDownloading()
                val progressFile = File(cacheDir, "$name.dat")
                val videoFile = File(directory, "$name.$type")
                var totalChunks: Long = 0
                if (progressFile.exists()) {
                    val `in` = FileInputStream(progressFile)
                    val data = DataInputStream(`in`)
                    totalChunks = data.readLong()
                    data.close()
                    `in`.close()
                    if (!videoFile.exists()) {
                        if (!videoFile.createNewFile()) {
                            throw DownloadFailException("Can't create file for download.")
                        }
                    }
                } else if (videoFile.exists()) {
                    downloadFinished("$name.$type", videoFile)
                } else {
                    if (!videoFile.createNewFile()) {
                        throw DownloadFailException("Can't create file for download.")
                    }
                    if (!progressFile.createNewFile()) {
                        throw DownloadFailException("Can't create progress file.")
                    }
                }
                if (videoFile.exists() && progressFile.exists()) {
                    while (true) {
                        prevDownloaded = 0
                        val website = intent.getStringExtra("website")
                        var chunkUrl: String? = null
                        when (website) {
                            "dailymotion.com" -> chunkUrl =
                                getNextChunkWithDailymotionRule(intent, totalChunks)
                            "vimeo.com" -> chunkUrl = getNextChunkWithVimeoRule(intent, totalChunks)
                            "twitter.com" -> chunkUrl =
                                getNextChunkWithM3U8Rule(intent, totalChunks)
                            "metacafe.com" -> chunkUrl =
                                getNextChunkWithM3U8Rule(intent, totalChunks)
                            "myspace.com" -> chunkUrl =
                                getNextChunkWithM3U8Rule(intent, totalChunks)
                        }
                        if (chunkUrl == null) {
                            if (!progressFile.delete()) {
                                Log.i("loremarTest", "can't delete progressFile")
                            }
                            downloadFinished("$name.$type", videoFile)
                            break
                        }
                        bytesOfChunk = ByteArrayOutputStream()
                        try {
                            val uCon = URL(chunkUrl).openConnection()
                            if (uCon != null) {
                                val `in` = uCon.getInputStream()
                                val readableByteChannel = Channels.newChannel(`in`)
                                val writableByteChannel = Channels.newChannel(
                                    bytesOfChunk
                                )
                                var read: Int
                                while (true) {
                                    if (stop) return
                                    val buffer = ByteBuffer.allocateDirect(1024)
                                    read = readableByteChannel.read(buffer)
                                    if (read != -1) {
                                        buffer.flip()
                                        writableByteChannel.write(buffer)
                                    } else {
                                        val vAddChunk = FileOutputStream(videoFile, true)
                                        vAddChunk.write(bytesOfChunk!!.toByteArray())
                                        val outputStream = FileOutputStream(progressFile, false)
                                        val dataOutputStream = DataOutputStream(outputStream)
                                        dataOutputStream.writeLong(++totalChunks)
                                        dataOutputStream.close()
                                        outputStream.close()
                                        Log.i(
                                            "loremarTest", "downloaded chunk " + totalChunks +
                                                    ": " + chunkUrl
                                        )
                                        break
                                    }
                                }
                                readableByteChannel.close()
                                `in`.close()
                                bytesOfChunk!!.close()
                            }
                        } catch (e: FileNotFoundException) {
                            downloadFinished("$name.$type", videoFile)
                            break
                        } catch (e: IOException) {
                            val sw = StringWriter()
                            e.printStackTrace(PrintWriter(sw))
                            Log.e("loremarTest", sw.toString())
                            if (onDownloadFailExceptionListener != null) {
                                onDownloadFailExceptionListener!!.onDownloadFailException(sw.toString())
                            }
                            break
                        }
                    }
                }
            } else {
                Log.e("loremarTest", "Can't create Download directory.")
                onDownloadFailExceptionListener!!.onDownloadFailException("Can't create Download directory.")
            }
        } catch (e: IOException) {
            val sw = StringWriter()
            e.printStackTrace(PrintWriter(sw))
            Log.e("loremarTest", sw.toString())
            onDownloadFailExceptionListener?.onDownloadFailException(sw.toString())
        } catch (e: DownloadFailException) {
            Log.e("loremarTest", e.toString())
            onDownloadFailExceptionListener?.onDownloadFailException(e.toString())
        } catch (e: Exception) {
            val sw = StringWriter()
            e.printStackTrace(PrintWriter(sw))
            Log.e("loremarTest", sw.toString())
            onDownloadFailExceptionListener?.onDownloadFailException(sw.toString())
        }
    }

    private fun getNextChunkWithDailymotionRule(intent: Intent, totalChunks: Long): String {
        val link = intent.getStringExtra("link")
        return link!!.replace("FRAGMENT".toRegex(), "frag(" + (totalChunks + 1) + ")")
    }

    private fun getNextChunkWithVimeoRule(intent: Intent, totalChunks: Long): String {
        val link = intent.getStringExtra("link")
        return link!!.replace("SEGMENT".toRegex(), "segment-" + (totalChunks + 1))
    }

    private fun getNextChunkWithM3U8Rule(intent: Intent, totalChunks: Long): String? {
        val link = intent.getStringExtra("link")
        val website = intent.getStringExtra("website")
        var line: String? = null
        try {
            val m3u8Con = URL(link).openConnection()
            val `in` = m3u8Con.getInputStream()
            val inReader = InputStreamReader(`in`)
            val buffReader = BufferedReader(inReader)
            while (buffReader.readLine().also { line = it } != null) {
                if ((website == "twitter.com" || website == "myspace.com") && line
                        ?.endsWith(".ts") == true
                ) {
                    break
                } else if (website == "metacafe.com" && line!!.endsWith(".mp4")) {
                    break
                }
            }
            if (line != null) {
                var l: Long = 1
                while (l < totalChunks + 1) {
                    buffReader.readLine()
                    line = buffReader.readLine()
                    l++
                }
            }
            buffReader.close()
            inReader.close()
            `in`.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return if (line != null) {
            val prefix: String
            when (website) {
                "twitter.com" -> {
                    Log.i(
                        "loremarTest", "downloading chunk " + (totalChunks + 1) + ": " +
                                "https://video.twimg.com" + line
                    )
                    "https://video.twimg.com$line"
                }
                "metacafe.com" -> {
                    prefix = link!!.substring(0, link.lastIndexOf("/") + 1)
                    Log.i(
                        "loremarTest", "downloading chunk " + (totalChunks + 1) + ": " + prefix +
                                line
                    )
                    prefix + line
                }
                "myspace.com" -> {
                    prefix = link!!.substring(0, link.lastIndexOf("/") + 1)
                    Log.i(
                        "loremarTest", "downloading chunk " + (totalChunks + 1) + ": " + prefix +
                                line
                    )
                    prefix + line
                }
                else -> null
            }
        } else {
            null
        }
    }

    interface OnDownloadFinishedListener {
        fun onDownloadFinished(downloadedFile: File?)
    }

    interface OnDownloadStopListener {
        fun onStopDownloading()
    }

    interface OnLinkNotFoundListener {
        fun onLinkNotFound()
    }

    override fun onDestroy() {
        downloadFile = null

        Thread.currentThread().interrupt()
        super.onDestroy()
    }

    interface onDownloadFailExceptionListener {
        fun onDownloadFailException(message: String?)
    }

    fun sendDOwnloadedFileBroadCast(filePath: String?) {
        val intent = Intent()
        intent.action = BROADCAST_DOWNLOAD_FINISHED_ACTION
        intent.putExtra("PATH", filePath)
        sendBroadcast(intent)
    }

    companion object {
        const val BROADCAST_DOWNLOAD_FINISHED_ACTION = "com.video.download.finished_action"
        var PREV_LINK = ""
        private var downloadFile: File? = null
        private var prevDownloaded: Long = 0
        private var downloadSpeed: Long = 0
        private var totalSize: Long = 0
        private var chunked = false
        private var bytesOfChunk: ByteArrayOutputStream? = null
        private var downloadNotifier: DownloadNotifier? = null
        private var downloadFolder: String? = null
        private var stop = false
        private var downloadThread: Thread? = null
        private var isPrivateDownload = false
        fun checkFileIsDownloaded(filename: String?): DownloadVideo? {
            val completedVideos = load(LMvdApp.getInstance())
            val completedVideosList = completedVideos!!.getVideos()
            if (completedVideosList != null && completedVideosList.size > 0) {
                for (x in completedVideosList.indices) {
                    val savedFileName = completedVideosList[x].storagefilename
                    if (savedFileName!!.contains(filename!!)) {
                        return completedVideosList[x]
                    }
                }
            }
            return null
        }

        private var onDownloadFinishedListener: OnDownloadFinishedListener? = null

        fun setOnDownloadFinishedListener(listener: OnDownloadFinishedListener?) {
            onDownloadFinishedListener = listener
        }

        fun setOnDownloadStopListener(listener: OnDownloadStopListener?) {
            onDownloadStopListener = listener
        }

        private var onLinkNotFoundListener: OnLinkNotFoundListener? = null
        fun setOnLinkNotFoundListener(listener: OnLinkNotFoundListener?) {
            onLinkNotFoundListener = listener
        }

        fun stop() {
            if (downloadNotifier != null) {
                downloadNotifier!!.cancel()
            }
            val downloadService: Intent? = LMvdApp.getInstance()?.getDownloadService()
            LMvdApp.getInstance()?.stopService(downloadService)
            forceStopIfNecessary()
        }

        fun stopFromNotification() {
            stop()
            if (onDownloadStopListener != null) {
                onDownloadStopListener!!.onStopDownloading()
            }
        }

        fun forceStopIfNecessary() {
            if (downloadThread != null && downloadThread!!.isAlive) {
                stop = true
            }
        }

        /**
         * Should be called every second
         *
         * @return download speed in bytes per second
         */
        fun getDownloadSpeed(): Long {
            return if (!chunked) {
                if (downloadFile != null) {
                    val downloaded = downloadFile!!.length()
                    downloadSpeed = downloaded - prevDownloaded
                    prevDownloaded = downloaded
                    return downloadSpeed
                }
                0
            } else {
                if (bytesOfChunk != null) {
                    val downloaded = bytesOfChunk!!.size().toLong()
                    downloadSpeed = downloaded - prevDownloaded
                    prevDownloaded = downloaded
                    return downloadSpeed
                }
                0
            }
        }

        /**
         * @return remaining time to download video in milliseconds
         */
        val remaining: Long
            get() = if (!chunked && downloadFile != null) {
                val remainingLength = totalSize - prevDownloaded
                1000 * (remainingLength / downloadSpeed)
            } else {
                0
            }

        @Throws(DownloadFailException::class, IOException::class)
        fun prepareTargetDirectory(): File {
            val downloadFolder: File = ThemeUtils.getPublicDownloadedVideoStorageDir()
            // File downloadFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            /* File privateFolder = StorageUtils.getPrivateAlbumStorageDir(null);
        if(isPrivateDownload && privateFolder != null && (privateFolder.exists() || privateFolder.mkdir() || privateFolder.createNewFile())
                && privateFolder.canWrite()){

        }*/if (downloadFolder != null && (downloadFolder.exists() || downloadFolder.mkdir() || downloadFolder.createNewFile())
                && downloadFolder.canWrite()
            ) {
                return downloadFolder
            }
            val externalStorage = Environment.getExternalStorageDirectory()
            val externalStorageState = Environment.getExternalStorageState()
            if ((externalStorage
                        != null) && (externalStorage.exists() || externalStorage.mkdir() || externalStorage.createNewFile())
                && externalStorage.canWrite()
                && externalStorageState == Environment.MEDIA_MOUNTED
            ) {
                return File(externalStorage, "Download")
            }
            val appExternal = LMvdApp.getInstance()!!.getExternalFilesDir(null)
            if ((appExternal
                        != null) && (appExternal.exists() || appExternal.mkdir() || appExternal.createNewFile())
                && appExternal.canWrite()
            ) {
                return File(appExternal, "Rocks Download")
            }
            val message: String
            message = when (externalStorageState) {
                Environment.MEDIA_UNMOUNTABLE -> "External storage is un-mountable."
                Environment.MEDIA_SHARED -> "USB mass storage is turned on. Can not mount external storage."
                Environment.MEDIA_UNMOUNTED -> "External storage is not mounted."
                Environment.MEDIA_MOUNTED_READ_ONLY -> "External storage is mounted but has no write access."
                Environment.MEDIA_BAD_REMOVAL -> "External storage was removed without being properly ejected."
                Environment.MEDIA_REMOVED -> "External storage does not exist. Probably removed."
                Environment.MEDIA_NOFS -> "External storage is blank or has unsupported filesystem."
                Environment.MEDIA_CHECKING -> "Still checking for external storage."
                Environment.MEDIA_EJECTING -> "External storage is currently being ejected."
                Environment.MEDIA_UNKNOWN -> "External storage is not available for some unknown reason."
                Environment.MEDIA_MOUNTED -> "External storage is mounted but for some unknown reason is not" +
                        " available."
                else -> "External storage is not available. No reason."
            }
            throw DownloadFailException(message)
        }

        fun getDownloadFolder(): String? {
            return try {
                val directory = prepareTargetDirectory()
                if (!directory.absolutePath.endsWith("/")) {
                    downloadFolder = directory.absolutePath + "/"
                } else {
                    downloadFolder = directory.absolutePath
                }
                downloadFolder
            } catch (e: DownloadFailException) {

                Toast.makeText(LMvdApp.getInstance()!!, "No downnload directory",Toast.LENGTH_LONG).show()
                null
            } catch (e: IOException) {
                Toast.makeText(LMvdApp.getInstance()!!, "Sorry! Error in creating folder",Toast.LENGTH_LONG).show()
                null
            }
        }

        fun setIsPrivateDownload(flag: Boolean) {
            isPrivateDownload = flag
        }

        private var onDownloadFailExceptionListener: onDownloadFailExceptionListener? = null
        private var onDownloadStopListener: OnDownloadStopListener? = null
        fun setOnDownloadFailExceptionListener(listener: onDownloadFailExceptionListener?) {
            onDownloadFailExceptionListener = listener
        }
    }



}