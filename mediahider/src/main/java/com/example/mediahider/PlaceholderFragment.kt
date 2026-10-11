package com.example.mediahider

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.mediahider.asynctask.MoveFileAsyncTaskForR
import com.example.mediahider.asynctask.MoveFileAsyntask
//import com.example.mediahider.asynctask.MoveMusicAsyncTaskForR
//import com.example.mediahider.asynctask.MoveMusicAsyntask
import com.example.mediahider.asynctask.MovePhotoAsyncTaskForR
import com.example.mediahider.asynctask.MovePhotoAsyntask
import com.example.mediahider.databinding.FragmentPlaceholderBinding
import com.example.mediahider.utils.PAGER_POSITION
import com.example.mediahider.utils.StorageHelper
import com.example.mediahider.utils.StorageHelper.getFilePathFromUri
import com.example.mediahider.utils.Utils
import com.example.mediahider.utils.onUploadClickListener


class PlaceholderFragment : Fragment() {

    private var pagerPosition: Int? = null
    var onUploadListener: onUploadClickListener? = null
    private val binding by lazy { FragmentPlaceholderBinding.inflate(layoutInflater) }
    private lateinit var videoPickerLauncher: ActivityResultLauncher<Intent>
    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>
    private lateinit var audioPickerLauncher: ActivityResultLauncher<Intent>
    private lateinit var modifyImagePermissionLauncher: ActivityResultLauncher<IntentSenderRequest>
    private lateinit var modifyVideoPermissionLauncher: ActivityResultLauncher<IntentSenderRequest>
    private lateinit var modifyAudioPermissionLauncher: ActivityResultLauncher<IntentSenderRequest>

    val uriList = ArrayList<Uri>()



    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            pagerPosition = it.getInt(PAGER_POSITION)
        }

        modifyImagePermissionLauncher = registerForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                lockPhotos()
            } else {
                Toast.makeText(context,"Permission denied",Toast.LENGTH_SHORT).show()
                uriList.clear()
            }
        }

        modifyVideoPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                lockVideos()
            } else {
                Toast.makeText(context,"Permission denied",Toast.LENGTH_SHORT).show()
                uriList.clear()
            }
        }

        modifyAudioPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                lockMusicFiles()
            } else {
                Toast.makeText(context,"Permission denied",Toast.LENGTH_SHORT).show()
                uriList.clear()
            }
        }



        videoPickerLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->

            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data


                if (data?.clipData != null) {

                    val count = data.clipData!!.itemCount

                    for (i in 0 until count) {
                        val videoUri = data.clipData!!.getItemAt(i).uri

                        uriList.add(videoUri)
                    }
                    if (StorageHelper.isR()) {
                        launchRenameRequest(modifyVideoPermissionLauncher)
                    } else {
                        lockVideos()
                    }
                }
            }
        }

        imagePickerLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data

                if (data?.clipData != null) {

                    val count = data.clipData!!.itemCount
                    for (i in 0 until count) {
                        val videoUri = data.clipData!!.getItemAt(i).uri
                        uriList.add(videoUri)

                        var path = activity?.let { getFilePathFromUri(it,videoUri) }

                    }
                    if (StorageHelper.isR()) {
                        launchRenameRequest(modifyImagePermissionLauncher)
                    } else {
                        lockPhotos()
                    }
                }
            }
        }

        audioPickerLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data

                Log.d("dikshamerijaan","$data")

                if (data?.clipData != null) {
                    // Multiple videos selected
                    val count = data.clipData!!.itemCount
                    for (i in 0 until count) {
                        val videoUri = data.clipData!!.getItemAt(i).uri
                        Log.d("dikshamerijaan","$videoUri")
                        uriList.add(videoUri)

                    }
                    if (StorageHelper.isR()) {
                        launchRenameRequest(modifyAudioPermissionLauncher)
                    } else {
                        lockMusicFiles()
                    }
                }
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.R)
    fun launchRenameRequest(launcher: ActivityResultLauncher<IntentSenderRequest>) {
        if(uriList.isNotEmpty()){
            val pi = context?.let { createWriteRequestForUris(it, uriList) }
            val request = pi?.intentSender?.let { IntentSenderRequest.Builder(it).build() }
            if (request != null) {
                launcher.launch(request)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    fun createWriteRequestForUris(context: Context, uriList: ArrayList<Uri>): PendingIntent {
        return MediaStore.createWriteRequest(context.contentResolver, uriList)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return binding.root
//        return inflater.inflate(R.layout.fragment_placeholder, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when(pagerPosition){
            0->{
                binding.hideTxt.text = "Hide photos"
            }
            1->{
                binding.hideTxt.text = "Hide videos"
            }
            2->{
                binding.hideTxt.text = "Hide audios"
            }
        }

        binding.hideData.setOnClickListener {
            when(pagerPosition){

                0->{
                    openImagePicker()
                }
                1->{
                    openVideoPicker()
                }
                2->{
                    openAudioPicker()
                }
            }
        }


    }

    private fun openVideoPicker() {
        uriList.clear()
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI).apply {
            type = "video/*"
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        }
        videoPickerLauncher.launch(intent)
    }
    private fun openImagePicker() {
        uriList.clear()

        val intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        }
        imagePickerLauncher.launch(intent)
    }
     private fun openAudioPicker() {
         uriList.clear()
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "audio/*"
            addCategory(Intent.CATEGORY_OPENABLE)
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        }
        audioPickerLauncher.launch(intent)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Int, onUploadClickListener: onUploadClickListener) =
            PlaceholderFragment().apply {
                arguments = Bundle().apply {
                    putInt(PAGER_POSITION, param1)
                }
                onUploadListener = onUploadClickListener
            }
    }


    private fun lockPhotos() {

            if (StorageHelper.isR()) {
                MovePhotoAsyncTaskForR(
                    activity,
                    uriList,
                    false
                ).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
            } else {
                MovePhotoAsyntask(
                    activity,
                    uriList,
                    false
                ).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
            }

    }

    fun lockVideos(){

        if (StorageHelper.isR()) {
            MoveFileAsyncTaskForR(
                activity,
                uriList,
                false,
                false
            ).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
        } else {
            MoveFileAsyntask(
                activity,
                uriList,
                false
            ).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
        }
    }

    fun lockMusicFiles(){
//        if (StorageHelper.isR()) {
//            MoveMusicAsyncTaskForR(
//                activity,
//                uriList,
//                false
//            ).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
//        } else {
//            MoveMusicAsyntask(
//                activity,
//                uriList,
//                false
//            ).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
//        }
    }

}