package com.appspartan.avxdownloader

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import android.text.TextUtils
import android.text.format.Formatter
import com.appspartan.avxdownloader.utils.LMvdApp
import java.io.File

class DownloadNotifier internal constructor(private val downloadServiceIntent: Intent) {
    private val ID = 77777
    private val handler: Handler
    private val notificationManager: NotificationManager
    private var downloadingRunnable: DownloadingRunnable? = null

    private inner class DownloadingRunnable() : Runnable {
        override fun run() {
            val downloadFolder: String? = DownloadManager.getDownloadFolder()
            if (downloadFolder != null) {
                val filename =
                    downloadServiceIntent.getStringExtra("name") + "." + downloadServiceIntent.getStringExtra(
                        "type"
                    )
                val NB: Notification.Builder
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NB = Notification.Builder(
                        LMvdApp.getInstance()?.applicationContext,
                        "download_0169"
                    )
                        .setStyle(Notification.BigTextStyle())
                } else {
                    NB = Notification.Builder(LMvdApp.getInstance()?.applicationContext)
                        .setSound(null)
                        .setPriority(Notification.PRIORITY_LOW)
                }
                val buttonIntent: Intent = Intent(
                    LMvdApp.getInstance()?.applicationContext,
                    DownloadNotificationButtonReciever::class.java
                )
                buttonIntent.putExtra("notificationId", ID)
                val btPendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    PendingIntent.getBroadcast(
                        LMvdApp.getInstance()?.applicationContext,
                        0,
                        buttonIntent,
                        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                    )
                }else{
                    PendingIntent.getBroadcast(
                        LMvdApp.getInstance()?.applicationContext,
                        0,
                        buttonIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                    )
                }
                /*
                PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(LMvdApp.getInstance().getApplicationContext(),
                        3, buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
*/
                val resultIntent: Intent = Intent(
                    LMvdApp.getInstance()?.applicationContext,
                    DownloaderMainScreen::class.java
                )
                resultIntent.putExtra("TAB", "PROGRESS")
                val contentIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    PendingIntent.getActivity(
                        LMvdApp.getInstance()?.applicationContext,
                        2, resultIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                    )
                }else{
                    PendingIntent.getActivity(
                        LMvdApp.getInstance()?.applicationContext,
                        2, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT
                    )
                }
                NB.setContentTitle("Downloading $filename")
                    .setSmallIcon(R.drawable.fm_notif)
                    .setLargeIcon(
                        BitmapFactory.decodeResource(
                            LMvdApp.getInstance()?.applicationContext?.resources,
                            R.mipmap.ic_launcher_round
                        )
                    )
                    .addAction(
                        R.drawable.ic_close_cross,
                        "STOP DOWNLOADING",
                        btPendingIntent
                    ) // #0
                    .setContentIntent(contentIntent)
                    .setOngoing(true)
                if (downloadServiceIntent.getBooleanExtra("chunked", false)) {
                    val file = File(downloadFolder, filename)
                    val downloaded: String
                    if (file.exists()) {
                        downloaded = Formatter.formatFileSize(
                            LMvdApp.getInstance()?.applicationContext,
                            file.length()
                        )
                    } else {
                        downloaded = "0KB"
                    }
                    NB.setProgress(100, 0, true)
                        .setContentText(downloaded)
                    notificationManager.notify(ID, NB.build())
                    handler.postDelayed(this, 1000)
                } else {
                    val file = File(downloadFolder, filename)
                    val sizeString = downloadServiceIntent.getStringExtra("size")
                    var progress = 0
                    if (!TextUtils.isEmpty(sizeString)) {
                        progress = Math.ceil(
                            file.length().toDouble() / sizeString!!.toLong()
                                .toDouble() * 100
                        ).toInt()
                        progress = if (progress >= 100) 100 else progress
                    }
                    val downloaded = Formatter.formatFileSize(
                        LMvdApp
                            .getInstance()?.applicationContext, file.length()
                    )
                    if (sizeString != null) {
                        val total = Formatter.formatFileSize(
                            LMvdApp.getInstance()?.applicationContext, sizeString.toLong()
                        )
                        NB.setProgress(100, progress, false)
                            .setContentText("$downloaded/$total   $progress%")
                    }
                    notificationManager.notify(ID, NB.build())
                    if(progress < 100) {
                        handler.postDelayed(this, 1000)
                    }
                }
            }
        }
    }

    fun notifyDownloading() {
        downloadingRunnable = DownloadingRunnable()
        downloadingRunnable!!.run()
    }

    fun notifyDownloadFinished() {
        handler.removeCallbacks((downloadingRunnable)!!)
        notificationManager.cancel(ID)
        val filename = (downloadServiceIntent.getStringExtra("name") + "." +
                downloadServiceIntent.getStringExtra("type"))
        val resultIntent: Intent = Intent(
            LMvdApp.getInstance()?.applicationContext,
            DownloaderMainScreen::class.java
        )
        resultIntent.putExtra("TAB", "STORAGE")
        val contentIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(
                LMvdApp.getInstance()?.applicationContext,
                2, resultIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        }else{
            PendingIntent.getActivity(
                LMvdApp.getInstance()?.applicationContext,
                2, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
        val NB: Notification.Builder
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NB = Notification.Builder(LMvdApp.getInstance()?.applicationContext, "download_0269")
                .setTimeoutAfter(1500)
                .setAutoCancel(true)
                .setOngoing(false)
                .setContentTitle("Download Finished")
                .setContentText(filename)
                .setSmallIcon(R.drawable.fm_notif)
                .setContentIntent(contentIntent)
                .setLargeIcon(
                    BitmapFactory.decodeResource(
                        LMvdApp.getInstance()?.applicationContext?.resources,
                        R.mipmap.ic_launcher_round
                    )
                )
            notificationManager.notify(8888, NB.build())
        } else {
            NB = Notification.Builder(LMvdApp.getInstance()?.applicationContext)
                .setTicker("Download Finished")
                .setAutoCancel(true)
                .setOngoing(false)
                .setPriority(Notification.PRIORITY_HIGH)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setSmallIcon(R.drawable.fm_notif)
                .setContentIntent(contentIntent)
                .setLargeIcon(
                    BitmapFactory.decodeResource(
                        LMvdApp.getInstance()?.applicationContext?.resources,
                        R.mipmap.ic_launcher_round
                    )
                )
            notificationManager.notify(8888, NB.build())
            notificationManager.cancel(8888)
        }
    }

    fun cancel() {
        if (downloadingRunnable != null) {
            handler.removeCallbacks(downloadingRunnable!!)
        }
        notificationManager.cancel(ID)
    }

    internal inner class ButtonReceiver() : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            DownloadManager.stop()
            val notificationId = intent.getIntExtra("notificationId", 0)
            // if you want cancel notification
            val manager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.cancel(notificationId)
        }
    }

    init {
        notificationManager = LMvdApp.getInstance()?.applicationContext?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(
                NotificationChannel(
                    "download_0169",
                    "Download Notification", NotificationManager.IMPORTANCE_LOW
                )
            )
            notificationManager.createNotificationChannel(
                NotificationChannel(
                    "download_0269",
                    "Download Notification", NotificationManager.IMPORTANCE_HIGH
                )
            )
            notificationManager.getNotificationChannel("download_0169")
                .setSound(null, null)
        }
        val thread = HandlerThread("downloadNotificationThread")
        thread.start()
        handler = Handler(thread.looper)
    }
}