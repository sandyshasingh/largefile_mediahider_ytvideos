package com.example.mediahider.fragments

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
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.mediahider.ActionModeListener
import com.example.mediahider.HiddenFiles
import com.example.mediahider.HiddenPhotosAdapter
import com.example.mediahider.HiddenVideosAdapter
import com.example.mediahider.OnVideoSelectedListener
import com.example.mediahider.asynctask.MoveFileAsyncTaskForR
import com.example.mediahider.asynctask.MoveFileAsyntask
import com.example.mediahider.databinding.FragmentHiddenVideoBinding
import com.example.mediahider.utils.AppThemePrefrences
import com.example.mediahider.utils.StorageHelper
import com.example.mediahider.utils.onUploadClickListener

class HiddenVideoFragment : Fragment(), OnVideoSelectedListener, ActionModeListener {

    var onUploadListener: onUploadClickListener? = null
    private val binding by lazy { FragmentHiddenVideoBinding.inflate(layoutInflater)}
    var videoPath = ""
    private val viewModel: HiddenFilesViewModel by viewModels()
    private lateinit var modifyVideoPermissionLauncher: ActivityResultLauncher<IntentSenderRequest>
    val uriList = ArrayList<Uri>()
    private lateinit var videoPickerLauncher: ActivityResultLauncher<Intent>
    var videosList : ArrayList<HiddenFiles> = ArrayList()
    var videosAdapter : HiddenVideosAdapter? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        modifyVideoPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                lockVideos()
            } else {
                Toast.makeText(context,"Permission denied", Toast.LENGTH_SHORT).show()
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

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_hidden_video, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.hideData.setOnClickListener {
            openVideoPicker()
        }

        binding.hideData2.setOnClickListener {
            openVideoPicker()
        }

        viewModel.videoFiles.observe(viewLifecycleOwner, Observer { files ->
            files.forEach {
                println("File: ${it.name}  URI: ${it.path}")
//                photosList = it as ArrayList<HiddenFiles>
//                setRV()
                Log.d("dikshababy","${it.name}  uri :${it.path}\n ${it.originalUri}")
            }

            videosList = ArrayList(files)

            // Now set your RecyclerView adapter or update
            setRV()
        })

        var documentUri = AppThemePrefrences.GetSharedPreference(context, AppThemePrefrences.HIDER_URI)
        viewModel.loadVideosFromUri(documentUri.toUri())



    }

    private fun setRV() {
        if(videosList.size>0)
        {
            var temp: ArrayList<HiddenFiles> = ArrayList()
            temp.addAll(videosList)
            binding.zrpVideos.visibility = View.GONE
            binding.hideData.visibility = View.VISIBLE

            binding.rvVideos.visibility = View.VISIBLE
            videosAdapter = HiddenVideosAdapter(
                temp,
                context,
                this@HiddenVideoFragment,
                this@HiddenVideoFragment,
            )
            binding.rvVideos.adapter = videosAdapter
        }
        if(videosList.isEmpty())
        {

            binding.zrpVideos.visibility = View.VISIBLE

            binding.hideData.visibility = View.GONE
            binding.rvVideos.visibility = View.GONE
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(videoPath: String, onUploadClickListener: onUploadClickListener) =
            HiddenVideoFragment().apply {
//                arguments = Bundle().apply {
//                    putInt(PAGER_POSITION, param1)
//                }
                this.videoPath = videoPath
                onUploadListener = onUploadClickListener
            }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    fun createWriteRequestForUris(context: Context, uriList: ArrayList<Uri>): PendingIntent {
        return MediaStore.createWriteRequest(context.contentResolver, uriList)
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

    private fun openVideoPicker() {
        uriList.clear()
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI).apply {
            type = "video/*"
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        }
        videoPickerLauncher.launch(intent)
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

    override fun onVideoSelected(hiddenVideos: HiddenFiles) {
        TODO("Not yet implemented")
    }

    override fun onVideoDeselected(hiddenVideos: HiddenFiles) {
        TODO("Not yet implemented")
    }

    override fun onVideoClicked(hiddenVideos: List<HiddenFiles>, position: Int) {
        TODO("Not yet implemented")
    }

    override fun onVideoFolderClicked(hiddenVideos: HiddenFiles) {
        TODO("Not yet implemented")
    }

    override fun getActionMode(): Boolean? {
        TODO("Not yet implemented")
    }

    override fun setActionModeValue(actionMode: Boolean) {
        TODO("Not yet implemented")
    }

    override fun onSelectionCountChanged(selectedCount: Int) {
        TODO("Not yet implemented")
    }
}