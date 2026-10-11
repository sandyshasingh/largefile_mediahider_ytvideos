package com.example.cleanfiles

import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.cleanfiles.databinding.FragmentCleanBinding
import kotlinx.coroutines.*
import java.io.File


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class CleanFragment : Fragment(), CoroutineScope by MainScope(),ItemBeingDeleted {
    private var param1: String? = null
    private var param2: String? = null
    var parentAdapter : ParentCleanmasterAdapter? = null
    var listForParentAdapter:ArrayList<CleanmasterItems> = ArrayList()
    var model : ApkViewModel? = null
    var list : ArrayList<ApkItem> = ArrayList()
    var logAndTemp : ArrayList<ApkItem> = ArrayList()
    var emptyFolders : ArrayList<ApkItem> = ArrayList()
    var deleteAllFiles : ArrayList<ApkItem> = ArrayList()

    private var cleanMasterActivity : CleanFilesActivity? = null
    var sizeLogAndTemp:Long = 0L
    var sizeEmptyFolder:Long = 0L
    var sizeApk:Long = 0L
    var sizeBeingDeleted:Long = 0L
    var totalJunkSize:Long = 0L
    var fetched = false
    var nojunk = false
    var cancel = true
    var size :String = ""
    private var animationDuration: Long = 1500L
    private var scaleFactor: Float = 1f
    var sizeToClean=""
    var completeList:ArrayList<ApkItem> = ArrayList()
    var handler : Handler? = null


    private val binding by lazy {  FragmentCleanBinding.inflate(layoutInflater) }

//    private var _binding: FragmentCleanBinding? = null // ViewBinding variable
//    private val binding get() = _binding!! // Property to access the binding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        mView = inflater.inflate(R.layout.fragment_clean, container, false)
        if (activity!=null && activity is CleanFilesActivity)
        {
            cleanMasterActivity = activity as CleanFilesActivity
            model = ViewModelProvider(cleanMasterActivity!!)[ApkViewModel::class.java]

        }
//        _binding = FragmentCleanBinding.inflate(inflater, container, false) // Inflate binding
        return binding.root // Return the root view of the binding


//        return mView
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if(activity != null){
            listForParentAdapter.add(
                CleanmasterItems(
                    "Trash Items",
                    0L,
                    null,
                    isDir = false,
                    icon = R.drawable.trash_item_icon
                )
            )
            listForParentAdapter.add(CleanmasterItems(
                "Useless files",
                0L,
                null,
                isDir = false,
                icon = R.drawable.useless_file_icon
            ))
            listForParentAdapter.add(CleanmasterItems(
                requireActivity().getString(R.string.empty_folders),
                0L,
                null,
                isDir = true,
                icon = R.drawable.ic_folder
            ))
            listForParentAdapter.add(CleanmasterItems(
                "Ad Junk",
                0L,
                null,
                isDir = false,
                icon = R.drawable.adjunk_icon
            ))
            listForParentAdapter.add(CleanmasterItems(
                requireActivity().getString(R.string.apks_clean),
                0L,
                null,
                isDir = false,
                icon = R.drawable.apk_icon
            ))
            parentAdapter = ParentCleanmasterAdapter(
                activity,
                listForParentAdapter,
                this,
            )
            binding.parentRecyclerViewCleanMaster.adapter = parentAdapter
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        launch {
            withContext(Dispatchers.IO) {
                if (context!=null)
                    model?.fetchApks(requireContext())
                filterTempAndLog(Environment.getExternalStorageDirectory())
                filterEmptyFolder(Environment.getExternalStorageDirectory())
                totalJunkSize = totalJunkSize.plus(sizeLogAndTemp)
                totalJunkSize = totalJunkSize.plus(sizeEmptyFolder)
                sizeBeingDeleted = sizeBeingDeleted.plus(sizeLogAndTemp)
                sizeBeingDeleted = sizeBeingDeleted.plus(sizeEmptyFolder)
                 size = format(totalJunkSize.toDouble(),2)

                fetched = true

                withContext(Dispatchers.Main)
                {
                    binding.totalSize.text = size
                    sizeToClean = size
                    parentAdapter?.listItem?.set(1, CleanmasterItems(
                        "Useless files",
                        sizeLogAndTemp,
                        logAndTemp,
                        true,
                        isDir = false,
                        icon = R.drawable.useless_file_icon
                    ))
                    parentAdapter?.notifyItemChanged(1)

                    parentAdapter?.listItem?.set(2, CleanmasterItems(
                        "Empty folders",
                        sizeEmptyFolder,
                        emptyFolders,
                        true,
                        isDir = true,
                        icon = R.drawable.ic_folder
                    ))
                    parentAdapter?.notifyItemChanged(2)

                    parentAdapter?.listItem?.set(0, CleanmasterItems(
                        "Trash Items",
                        0L,
                        null,
                        true,
                        isDir = false,
                        icon = R.drawable.trash_item_icon
                    ))
                    parentAdapter?.notifyItemChanged(0)

                    parentAdapter?.listItem?.set(3, CleanmasterItems(
                        "AD junk",
                        0L,
                        null,
                        true,
                        isDir = false,
                        icon = R.drawable.adjunk_icon
                    ))
                    parentAdapter?.notifyItemChanged(3)

                    binding.loaderJunkfiles.pauseAnimation()
                    binding.loaderJunkfiles.visibility = View.GONE
                    binding.headingLoader.visibility = View.GONE
//                    binding.loadingScreen.visibility = View.GONE
//                    files_loaded?.visibility = View.VISIBLE
                    if (fetched)
                    {

                        cancel = false


                        var size = format(totalJunkSize.toDouble(),2)
                        if (size.startsWith("0.00")){
                            binding.btmBtn.text = "No junk "
                            nojunk = true

                        }

                        else
                            binding.btmBtn.text =  "Clean Up"

                    }
                }
            }



        }
        model?.apps?.observe(cleanMasterActivity!!, androidx.lifecycle.Observer {
            if (it != null) {
                list = it as ArrayList<ApkItem>
                sizeApk = 0
                for (i in list)
                    sizeApk = sizeApk.plus(i.size)
                parentAdapter?.listItem?.set(4, CleanmasterItems(
                    "Apks",
                    sizeApk,
                    list,
                    true,
                    isDir = false,
                    icon = R.drawable.icon_apknew
                ))
                parentAdapter?.notifyItemChanged(4)
                totalJunkSize = 0
                sizeBeingDeleted = 0
                totalJunkSize = totalJunkSize.plus(sizeApk)

                sizeBeingDeleted = sizeBeingDeleted.plus(sizeApk)
                var size = format(totalJunkSize.toDouble(),2)
                binding.totalSize.text = "$size"

            }
        })


//        binding.backkIconLoad.setOnClickListener {
//            activity?.onBackPressed()
//        }


        binding.deleteJunk.setOnClickListener {

                if (nojunk)
                    (activity as CleanFilesActivity).onBackPressed()
                else if (cancel){


                    (activity as CleanFilesActivity).onBackPressed()

                }
                else{

//                    if(activity != null){
//                        if (!RemoteConfigUtils.getNewInterstitialAdFlow(requireActivity())) {
//                            if (InterstitialAdSingeleton.getInstance().mInterstitialAd != null && activity != null) {
//                                showInterstitialAd(activity) {}
//                            }
//                        }
//                    }

                    binding.mloader.visibility = View.VISIBLE
                    binding.deleteJunk.isEnabled = false
                    binding.deleteJunk.isClickable = false
                    launch {
                        withContext(Dispatchers.IO) {
                            deleteAlljunk{
                                (activity as CleanFilesActivity?)?.loadFinalFragment(sizeToClean, completeList)
                            }
                        }
                    }

                }

//            parentAdapter?.notifyItemChanged(4)
            }


        binding.backk.setOnClickListener {
            totalJunkSize = 0L
            sizeBeingDeleted = 0L
            (activity as CleanFilesActivity).onBackPressed()
        }
    }




    fun format(bytes: Double, digits: Int): String {

        var byte = bytes
        val dictionary = arrayOf("B", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB")
        var index = 0
        while (index < dictionary.size) {
            if (byte < 1024) {
                break
            }
            byte /= 1024
            index++
        }
        return String.format("%." + digits + "f", byte)  + " " +dictionary[index]
    }

    private fun filterEmptyFolder(dir: File) {
        val FileList = dir.listFiles()
        if (FileList != null) {
            for (i in FileList.indices) {
                if (FileList[i].isDirectory){
                    if ( File(FileList[i].absolutePath).listFiles()?.isEmpty() == true) {

                        emptyFolders.add(ApkItem(
                            FileList[i].absolutePath,
                            FileList[i].name,
                            FileList[i].length(),
                            FileList[i].lastModified(),
                            sizeInFormat = FileList[i].length().let { format(it.toDouble(), 2) }
                        ))
                        sizeEmptyFolder = sizeEmptyFolder.plus(FileList[i].length())
                    }
                    else
                        filterEmptyFolder(FileList[i])
                }

            }
        }
    }

    fun deleteAlljunk( callback: () -> Unit){
        deleteAllFiles.addAll(list)
        deleteAllFiles.addAll(logAndTemp)
        deleteAllFiles.addAll(emptyFolders)


//        completeList.addAll(deleteAllFiles)

        for (i in deleteAllFiles){
            if (i.isSelected){

                try {
                    completeList?.add(i)

                    val where = "${MediaStore.MediaColumns.DATA} = ?"
                    val args = arrayOf(i.path)
                    activity?.contentResolver?.delete(MediaStore.Files.getContentUri("external"), where, args) != 1
                    File(i.path).delete()
                }
                catch (e : Exception){
                }
            }

        }
        callback()
    }
    fun filterTempAndLog(dir: File) {

        val tmpPattern = ".tmp"
        val logPattern = ".log"
        val FileList = dir.listFiles()
        if (FileList != null) {
            for (i in FileList.indices) {
                if (FileList[i].isDirectory) {
                    filterTempAndLog(FileList[i])
                } else {
                    if (FileList[i].name.endsWith(tmpPattern) || FileList[i].name.endsWith(logPattern)) {
                        logAndTemp.add(ApkItem(
                            FileList[i].absolutePath,
                            FileList[i].name,
                            FileList[i].length(),
                            FileList[i].lastModified(),
                            sizeInFormat =  FileList[i].length().let { format(it.toDouble(), 2) }
                        ))
                        sizeLogAndTemp = sizeLogAndTemp.plus(FileList[i].length())
                    }
                }
            }
        }

    }


    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CleanFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun beingDeleted(addSize: Boolean, size: Long) {
        if (addSize)
            sizeBeingDeleted = sizeBeingDeleted.plus(size)
        else
            sizeBeingDeleted = sizeBeingDeleted.minus(size)

         sizeToClean = format(sizeBeingDeleted.toDouble(),2)

        binding.totalSize.text = sizeToClean

        binding.btmBtn.text = "Clean Up"

    }
}