package com.appspartan.avxdownloader

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.text.TextUtils
import android.text.format.Formatter
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.appspartan.avxdownloader.utils.LMvdApp
import com.appspartan.avxdownloader.utils.Presenter
import com.appspartan.avxdownloader.utils.ThemeUtils
import com.appspartan.avxdownloader.utils.createCacheFile
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.io.IOException
import java.lang.Exception
import java.net.URL
import java.net.URLConnection

const val IMAGE = "image"


class BottomSheetUtility {

    companion object{

        fun createBottomSheet(activity: Activity?): BottomSheetDialog {
            return BottomSheetDialog(activity!!, R.style.CustomBottomSheetDialogTheme)
        }

        fun imageParserBottomSheet(
            activity: Activity,
            imageSrc: String?,
            title: String,
            website: String?
        ) {
            Presenter(object : Presenter.OnCoroutineScope {
                var fbCon: URLConnection? = null
                var image: Bitmap? = null
                var size: String? = null
                var bitmapPath: String? = null
                var preFetchDialog: BottomSheetDialog? = null
                override fun doInBackground() {
                    try {
                        fbCon = URL(imageSrc).openConnection()
                        fbCon?.connect()
                        val url = URL(imageSrc)
                        image = BitmapFactory.decodeStream(url.openConnection().getInputStream())
                        size = fbCon?.getHeaderField("content-length")
                        bitmapPath = image?.createCacheFile(activity)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }

                override fun onPreExecute() {
                    if (ThemeUtils.getActivityIsAlive(activity)) {
                        val dialogView: View =
                            activity.layoutInflater.inflate(R.layout.bs_prefetch, null)
                        preFetchDialog = createBottomSheet(activity)
                        preFetchDialog!!.setContentView(dialogView)
                        val textHeading = preFetchDialog?.findViewById<TextView>(R.id.txtHeading)
                        textHeading?.text = "Image download"

                        preFetchDialog?.show()
                    }
                }

                override fun onPostExecute() {

                    if (ThemeUtils.getActivityIsAlive(activity)) {
                        if (preFetchDialog != null) {
                            preFetchDialog!!.dismiss()
                        }
                        activity.runOnUiThread {
                            val dialogView: View =
                                activity.layoutInflater.inflate(
                                    R.layout.image_download_parser,
                                    null
                                )
                            val dialog =
                                createBottomSheet(activity)
                            dialog.setContentView(dialogView)
                            dialog.show()
                            dialog.setCanceledOnTouchOutside(true)
                            val header =
                                dialog.findViewById<View>(R.id.txtHeaderTitle) as TextView?
                            header!!.text = title


                            val fileNameTextView =
                                dialog.findViewById<EditText>(R.id.videofileName)
                            val millis = System.currentTimeMillis()
                            val fileName = title + "_" + millis
                            if (!TextUtils.isEmpty(fileName)) {
                                fileNameTextView!!.setText(fileName)
                            }
                            val thumbnailImageView =
                                dialogView.findViewById<View>(R.id.imageThumbnail) as ImageView
                            val downloadCompletedSize =
                                dialog.findViewById<View>(R.id.downloadCompletedSize) as TextView?
                            val downloadCompletedExt =
                                dialog.findViewById<View>(R.id.downloadCompletedExt) as TextView?
                            if (image != null) {
                                thumbnailImageView.setImageBitmap(image)
                            }
                            if (!TextUtils.isEmpty(size)) {
                                val sizeFormatted = Formatter.formatShortFileSize(
                                    activity,
                                    size!!.toLong()
                                )
                                downloadCompletedExt!!.text = "Size $sizeFormatted | jpg format"
                                downloadCompletedSize!!.text = sizeFormatted
                            }
                            val downloadButton =
                                dialog.findViewById(R.id.downloadButton) as View?
                            val finalImage = image
                            downloadButton!!.setOnClickListener {
                                if (dialog != null) {
                                    dialog.dismiss()
                                }
                                if (ThemeUtils.getActivityIsAlive(activity)) {
                                    try {
                                        Log.d("dikshamyjaan","129 utility")

                                        val queues = DownloadQueuesNew.load(activity)
                                        Log.d("dikshamyjaan","130 utility")

                                        var userDefinedFileName = ""
                                        if (fileNameTextView != null && fileNameTextView.text != null) {
                                            val fileName = fileNameTextView.text.toString()
                                            userDefinedFileName =
                                                if (!TextUtils.isEmpty(fileName)) {
                                                    fileName
                                                } else {
                                                    fileName
                                                }
                                        }
                                        Log.d("dikshamyjaan","151 utility  queue: $queues   imgsrc:  $imageSrc  website:  $website  bitmap:  $bitmapPath")
                                        queues.insertToTop(
                                            "" + size, "jpg",
                                            imageSrc!!,
                                            userDefinedFileName,
                                            imageSrc, false,
                                            website!!, bitmapPath!!, IMAGE, false
                                        )
//                                        Log.d("dikshamyjaan","151 utility  $queues")

                                        queues.save(activity)
                                        if (!ThemeUtils.isServiceRunning(
                                                DownloadManager::class.java,
                                                activity
                                            ) /*&& queues.getList().size()<2*/) {
                                            val topVideo = queues.getTopVideo()
                                            Log.d("dikshamyjaan","159  ${LMvdApp.getInstance()}  ")

                                            val downloadService = LMvdApp.getInstance()!!
                                                .getDownloadService()
                                            DownloadManager.stop()
                                            Log.d("dikshamyjaan","162 utility  $downloadService")

                                            downloadService!!.putExtra("link", topVideo!!.link)
                                            downloadService.putExtra("name", topVideo.name)
                                            downloadService.putExtra("type", topVideo.type)
                                            downloadService.putExtra("size", topVideo.size)
                                            downloadService.putExtra("page", topVideo.page)
                                            downloadService.putExtra("chunked", topVideo.chunked)
                                            downloadService.putExtra("website", topVideo.website)
                                            Log.d("dikshamyjaan","171 utility  $downloadService")

                                            LMvdApp.getInstance()!!.startService(downloadService)
                                            Log.d("dikshamyjaan","174 utility  ${LMvdApp.getInstance()}")

                                        }
                                    } catch (e: Exception) {
                                        Log.d("dikshamyjaan","166 utility $e")

                                        Toast.makeText(
                                            activity,
                                            "Download Failed: $e", Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }
                            }
                            dialog.findViewById<View>(R.id.bs_cancel)!!.setOnClickListener {
                                if (dialog != null) {
                                    dialog.dismiss()
                                }
                            }
                        }
                    }
                }
            }, 200).startTask()
        }
    }

}