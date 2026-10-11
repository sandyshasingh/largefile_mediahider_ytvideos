package com.example.mediahider.fragments

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mediahider.ActionModeListener
import com.example.mediahider.HiddenFiles
import com.example.mediahider.HiddenPhotosAdapter
import com.example.mediahider.OnImageSelectionListener
import com.example.mediahider.OnSelectionChangedListener
import com.example.mediahider.R
import com.example.mediahider.asynctask.MovePhotoAsyncTaskForR
import com.example.mediahider.asynctask.MovePhotoAsyntask
import com.example.mediahider.databinding.FragmentHiddenPhotoBinding
import com.example.mediahider.utils.AppThemePrefrences
import com.example.mediahider.utils.StorageHelper
import com.example.mediahider.utils.StorageHelper.getFilePathFromUri
import com.example.mediahider.utils.StorageUtils
import com.example.mediahider.utils.Utils
import com.example.mediahider.utils.doGone
import com.example.mediahider.utils.doVisible
import com.example.mediahider.utils.onUploadClickListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class HiddenPhotoFragment : Fragment(), CoroutineScope by MainScope(), OnImageSelectionListener, ActionModeListener,
    OnSelectionChangedListener {

    var onUploadListener: onUploadClickListener? = null

    private val viewModel: HiddenFilesViewModel by viewModels()
    var isActionMode : Boolean = false

    var imagePath = ""
    private val binding by lazy {  FragmentHiddenPhotoBinding.inflate(layoutInflater) }
    private lateinit var modifyImagePermissionLauncher: ActivityResultLauncher<IntentSenderRequest>
    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>
    val uriList = ArrayList<Uri>()
    var photosList : ArrayList<HiddenFiles> = ArrayList()
    var photosAdapter : HiddenPhotosAdapter? = null
    var selectedImages : ArrayList<HiddenFiles> = ArrayList()




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        modifyImagePermissionLauncher = registerForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                lockPhotos()
            } else {
                Toast.makeText(context,"Permission denied", Toast.LENGTH_SHORT).show()
                uriList.clear()
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
                    }
                    if (StorageHelper.isR()) {
                        launchRenameRequest(modifyImagePermissionLauncher)
                    } else {
                        lockPhotos()
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        return inflater.inflate(R.layout.fragment_hidden_photo, container, false)
        return binding.root
    }



    @RequiresApi(Build.VERSION_CODES.R)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.hideData.setOnClickListener {
            openImagePicker()
        }

        binding.hideData2.setOnClickListener {
            openImagePicker()
        }

        binding.bottomView.llDelete.setOnClickListener {
            var items = photosAdapter?.getSelectedItems()
            Utils.deleteMultipleImagesFromPathR(context,items)
        }

        binding.bottomView.llUnhide.setOnClickListener {
            Log.d("sandy","138 aa gaya")
            var items = photosAdapter?.getSelectedItems()
            if (items!=null)
              unlockPhotos(items!!)

        }


        viewModel.mediaFiles.observe(viewLifecycleOwner, Observer { files ->

            photosList = ArrayList(files)

            // Now set your RecyclerView adapter or update
            setRV()
        })

        var documentUri = AppThemePrefrences.GetSharedPreference(context, AppThemePrefrences.HIDER_URI)
        viewModel.loadFilesFromUri(documentUri.toUri())


    }

    private fun setRV() {
        if(photosList.isNotEmpty())
        {
            binding.zrpPhotos.visibility = View.GONE
            binding.hideData.visibility = if (isActionMode) View.GONE else View.VISIBLE
            binding.recyclerViewPhoto.visibility = View.VISIBLE

            photosAdapter = HiddenPhotosAdapter(photosList, context, this, this, this)
            binding.recyclerViewPhoto.adapter = photosAdapter

//            var temp: ArrayList<HiddenFiles> = ArrayList()
//            temp.addAll(photosList)
//            binding.zrpPhotos.visibility = View.GONE
//            binding.hideData.visibility = View.VISIBLE
//
//            binding.recyclerViewPhoto.visibility = View.VISIBLE
//            photosAdapter = HiddenPhotosAdapter(
//                temp,
//                context,
//                this@HiddenPhotoFragment,
//                this@HiddenPhotoFragment,
//            )
//            binding.recyclerViewPhoto.adapter = photosAdapter
        }
        else{
            binding.zrpPhotos.visibility = View.VISIBLE
            binding.hideData.visibility = View.GONE
            binding.recyclerViewPhoto.visibility = View.GONE
        }
//        if(photosList.isEmpty())
//        {
//            binding.zrpPhotos.visibility = View.VISIBLE
//
//            binding.hideData.visibility = View.GONE
//            binding.recyclerViewPhoto.visibility = View.GONE
//        }
    }

    private fun openImagePicker() {
        uriList.clear()

        val intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        }
        imagePickerLauncher.launch(intent)
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

    private fun unlockPhotos(list : ArrayList<Uri>) {

        if (StorageHelper.isR()) {
            MovePhotoAsyncTaskForR(
                activity,
                list,
                true
            ).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
        } else {
            MovePhotoAsyntask(
                activity,
                list,
                true
            ).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
        }

    }


    private fun getChildFiles() {
        if(context != null)
        {
            activity?.runOnUiThread {
                if(context != null)
                    binding.progressBar.visibility = View.VISIBLE
            }
            Log.d("dikshajaanu","213  $imagePath")
            val dir = File(imagePath)
            val newList = dir.listFiles()

            val newFolderList : ArrayList<HiddenFiles> = ArrayList()
            val newFileList : ArrayList<HiddenFiles> = ArrayList()
            Log.d("dikshajaanu","219  ${newList.size}")
            if( newList != null)
            {

                Log.d("dikshajaanu","225")
                for(i in newList)
                {
                    Log.d("dikshajaanu","228")
                    if(i.isFile)
                    {
                        Log.d("dikshajaanu","231")
                        val fileName = StorageUtils.decode(i.name,StorageUtils.offset)
                        val ext =   fileName?.substringAfterLast(".")
                        val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext?.lowercase())
                        val data = HiddenFiles(i.path,fileName!!,null ,StorageHelper.format(i.length().toDouble(), 1),  mimeType,0,false,true,0)
//                        data.updateTime = hiddenFilesDao?.getUpdateTimeForFile(i.path)?:0
//                        data.originalPath = hiddenFilesDao?.getOriginalPathForFile(i.path)?:""
                        Log.d("dikshajaanu","235  $fileName")
                        newFileList.add(data)
                    }
                    else
                    {
                        Log.d("dikshajaanu","243")
                        val list = i.listFiles()
                        val count = list?.count()?:0
                        Log.d("dikshajaanu","246")
                        if(count > 0)
                        {
                            Log.d("dikshajaanu","249")
                            val thumbnail = list?.get(0)?.path
                            Log.d("dikshajaanu","245  ${i.path}")
                            newFolderList.add(HiddenFiles(i.path,i.name,null,"","image/*",0,false,false,count))
                        }
                    }
                }
                photosList.addAll(newFolderList)
                if(newFileList.size>0)
                {
                    newFileList.sortedBy { fileDataClass -> fileDataClass.updateTime }.reversed().let {
                        if(it.isNotEmpty())
                            photosList.addAll(it)
                    }
                }
            }
            activity?.runOnUiThread {
                binding.progressBar.visibility = View.GONE
//            progressWheel?.dismiss()
//            progressWheel = null
            }
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

    companion object {
        @JvmStatic
        fun newInstance(photoPath: String, onUploadClickListener: onUploadClickListener) =
            HiddenPhotoFragment().apply {
                imagePath = photoPath
                onUploadListener = onUploadClickListener
            }
    }

    override fun onImageDeselected(hiddenPhotos: HiddenFiles) {
        selectedImages.remove(hiddenPhotos)
        if (selectedImages.isEmpty()) setActionModeValue(false)

        /*selectedImages.remove(hiddenPhotos)
        if(selectedImages.size == 0)
        {
            setActionModeValue(false)
        }*/
//        binding.imgSelectAll.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.img_deselect, null))

    }

    override fun onImageSelected(hiddenPhotos: HiddenFiles) {
        selectedImages.add(hiddenPhotos)
        if (!isActionMode) setActionModeValue(true)

        /*selectedImages.add(hiddenPhotos)*/
//        if(selectedImages.size == photosList.size)
//        {
//            selectAll = true
//            binding.imgSelectAll.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_select, null))
//        }
       /* binding.bottomView.root.doVisible()
//        binding.btnUpload.doGone()
        binding.hideData.doGone()*/
//        binding.rlSelectAll.doVisible()
    }

    override fun getActionMode(): Boolean = isActionMode


    override fun onImageClicked(hiddenPhotos: List<HiddenFiles>, position: Int) {
//        (activity as PhotosActivity).viewFile(hiddenPhotos,position)
    }

    override fun onImageFolderClicked(hiddenPhotos: HiddenFiles) {
//        doExit = false
//        photosList.clear()
//        currentPath = hiddenPhotos.path
       /* launch {
            withContext(Dispatchers.IO)
            {
//                getChildFiles()
                withContext(Dispatchers.Main)
                {

                    photosAdapter?.photosList = photosList
                    if(photosList.isEmpty()){
                        binding.zrpPhotos.visibility = View.VISIBLE
//                        binding.btnUpload.visibility = View.GONE
                        binding.hideData.doGone()
                        binding.recyclerViewPhoto.visibility = View.GONE
                    }
                    photosAdapter?.notifyDataSetChanged()
                }
            }


        }*/
    }



    override fun setActionModeValue(actionMode: Boolean) {
        isActionMode = actionMode

        binding.bottomView.root.visibility = if (actionMode) View.VISIBLE else View.GONE
        binding.hideData.visibility = if (!actionMode && photosList.isNotEmpty()) View.VISIBLE else View.GONE

        if (!actionMode) {
            selectedImages.clear()
            photosList.forEach { it.isSelected = false }
            photosAdapter?.notifyDataSetChanged()
        }

        /*isActionMode = actionMode
        if(!actionMode)
        {
//            selectAll = false
//            binding.imgSelectAll.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.img_deselect, null))
            for(i in photosList)
                i.isSelected = false
            selectedImages.clear()
//            binding.rlSelectAll.doGone()


            if (photosList.isNotEmpty()){
                binding.hideData.doVisible()
//                binding.photoStrip.doVisible()
            }

            binding.bottomView.root.doGone()
            photosAdapter?.notifyDataSetChanged()
        }*/
    }

    override fun onSelectionCountChanged(selectedCount: Int) {

    }

    override fun onSelectionChanged(selectedItems: List<HiddenFiles>) {
        if (selectedItems.isNotEmpty()) {
            binding.bottomView.root.visibility = View.VISIBLE
            binding.hideData.visibility = View.GONE
        } else {
            binding.bottomView.root.visibility = View.GONE
            binding.hideData.visibility = View.VISIBLE
        }

    }
}