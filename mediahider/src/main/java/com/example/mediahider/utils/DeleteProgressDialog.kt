package com.example.mediahider.utils

import android.app.ProgressDialog
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.WindowManager

class DeleteProgressDialog (val context: Context?) {
    private var progress: ProgressDialog? = null
    var progressStatus=1
    var progressMax=100
    var thread:Thread?=null
    var totalProgressTime = 100

    fun startDeleting(title : String) {
        progress = ProgressDialog(context)
        progress?.setMessage(title)
        progress?.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        progress?.window?.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        progress?.progress = 0
        progress?.show()
        thread = object : Thread() {
            override fun run() {
                while (progressStatus < totalProgressTime) {
                    try {
                        sleep(100)
                        progress?.max=progressMax
                        progress?.progress = progressStatus
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }

            }
        }
        thread?.start()
    }

    fun dismiss() {
        Handler(Looper.getMainLooper()).postDelayed( {

            try {
                if (progress != null && progress?.isShowing == true) {
                    progress?.dismiss()
                }
            }catch (e:Exception){
                progress=null
            }


        },100)

    }

    fun setValues(pos: Int, size: Int) {
        progressStatus=pos
        totalProgressTime=size
        progressMax=size
    }


}