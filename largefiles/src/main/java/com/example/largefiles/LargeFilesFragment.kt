package com.example.largefiles

import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.largefiles.databinding.FragmentLargeFilesBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

const val PARAM_ID = "idExtra"


class LargeFilesFragment : Fragment(), CoroutineScope by MainScope(), AllitemSelected,ItemBeingDeleted {

    private val binding by lazy {  FragmentLargeFilesBinding.inflate(layoutInflater) }
    var model : LargeFileViewModel? = null
    var listLargeItm : ArrayList<LargeItem> = ArrayList()
    var listSelectedItm : ArrayList<LargeItem> = ArrayList()
    var largeAdapter : LargeFilesAdapter? = null
    var idType = VIDEOS_ID
    private var mProgressDialog: AppProgressDialog? = null
    var sizeBeingDeleted:Long = 0L
    var sizeToClean=""
    var selectAll = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bundle = arguments
        idType = bundle?.getInt(PARAM_ID)?: VIDEOS_ID

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        model = activity?.let { ViewModelProvider(it) }?.get(LargeFileViewModel::class.java)

        return binding.root
//        inflater.inflate(R.layout.fragment_large_files, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        itemClicked(idType)
        binding.cleanLargeFiles.setOnClickListener {
            launch {
                withContext(Dispatchers.IO) {
                    deleteLargeFiles {
                        (activity as LargeFileMainActivity)?.openfilesCleaned(format(sizeBeingDeleted.toDouble(),2))
                    }
                }
            }

        }
        binding.allSelected.setOnClickListener {

            //////////

            lifecycleScope.launch(Dispatchers.Default) {
                sizeBeingDeleted = 0L
                selectAll = !selectAll
                listSelectedItm.clear()
                if(selectAll)
                    listSelectedItm.addAll(listLargeItm)
                else
                    listSelectedItm.removeAll(listLargeItm)
                for(item in listLargeItm){
                    item.isSelected = selectAll
                    sizeBeingDeleted += item.size
                }

                val formattedSize = format(sizeBeingDeleted.toDouble(), 2)

                withContext(Dispatchers.Main) {

                    if(selectAll) {
                        binding.cleanFileSize.text = "Clean Up $formattedSize"
                    }else{
                        sizeBeingDeleted = 0L
                        binding.cleanFileSize.text = "Clean Up 0 MB"
                    }
                    largeAdapter?.listItem = listLargeItm
                    largeAdapter?.notifyDataSetChanged()
                }
            }

            ///////////

        }

        binding.backMain.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(idType: Int) : LargeFilesFragment{
            val myFragment = LargeFilesFragment()
            val bundle = Bundle()
            bundle.putInt(PARAM_ID, idType)
            myFragment.arguments = bundle
            return myFragment
        }

    }
    fun itemClicked(itemClicked:Int?) {
        when (itemClicked) {

            VIDEOS_ID -> {

                showDialog()
                binding.filterHeading.text = "Larger thann 25 MB"

                if (context != null)
                    model?.fetchLargeVideo(context!!)

                model?.videoData?.observe(requireActivity()) { listData ->
                    if (context != null) {

                        dismissDialog()
                        if (listData != null) {
                            listLargeItm = listData
//                            listSelectedItm = listData
                        }

                        largeAdapter = LargeFilesAdapter(
                            activity,
                            listLargeItm,
                            this,
                            this,

                            )
                        binding.itemListRv.adapter = largeAdapter

//                        lifecycleScope.launch(Dispatchers.Default) {
//                            val list = listData as ArrayList<LargeItem>
//                            var sizeLargeVideo: Long = 0
//                            for (i in list)
//                                sizeLargeVideo += i.size
//
//                            sizeBeingDeleted = sizeLargeVideo
//
//                            val formattedSize = format(sizeBeingDeleted.toDouble(), 2)
//
//                            withContext(Dispatchers.Main) {
//
//                                binding.cleanFileSize.text = "Clean Up $formattedSize"
//
//                            }
//                        }



                    }
                }
            }

            PHOTOS_ID -> {

                showDialog()
                binding.filterHeading.text = "Larger thann 5 MB"

                if (context != null)
                    model?.fetchLargeImage(context!!)

                model?.imageData?.observe(requireActivity()) { listData ->
                    if (context != null) {
                        if (listData != null) {
                            listLargeItm = listData
//                            listSelectedItm = listData
                        }
                        dismissDialog()
                        largeAdapter = LargeFilesAdapter(
                            activity,
                            listLargeItm,
                            this,
                            this,

                            )
                        binding.itemListRv.adapter = largeAdapter

//                        lifecycleScope.launch(Dispatchers.Default) {
//                            val list = listData as ArrayList<LargeItem>
//                            var sizeLargeImage: Long = 0
//                            for (i in list)
//                                sizeLargeImage += i.size
//
//                            sizeBeingDeleted = sizeLargeImage
//
//                            val formattedSize = format(sizeBeingDeleted.toDouble(), 2)
//
//                            withContext(Dispatchers.Main) {
//
//                                binding.cleanFileSize.text = "Clean Up $formattedSize"
//
//                            }
//                        }


                    }
                }
            }
        }
    }

    fun deleteLargeFiles(callback: () -> Unit){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            Log.d("dikshamyjaan","227 ")
        }
        else{
            for (i in listSelectedItm){
                if (i.isSelected){

                    try {
                        val where = "${MediaStore.MediaColumns.DATA} = ?"
                        val args = arrayOf(i.path)
                        activity?.contentResolver?.delete(MediaStore.Files.getContentUri("external"), where, args) != 1
                        File(i.path).delete()
                    }
                    catch (e : Exception){
                    }
                }

            }
        }


        callback()
    }

    private fun showDialog() {
        try {
            //Dismiss if already exist
//            dismissDialog()
            if (ThemeUtils.getActivityIsAlive(activity)) {
                mProgressDialog = AppProgressDialog(activity)
                mProgressDialog?.setCancelable(true)
                mProgressDialog?.setCanceledOnTouchOutside(true)
                mProgressDialog?.show()
            }
        } catch (e: Exception) {
        }
    }

    private fun dismissDialog() {
        if (ThemeUtils.getActivityIsAlive(activity) && mProgressDialog != null && mProgressDialog!!.isShowing) {
            mProgressDialog!!.dismiss()
        }
    }

    override fun onAllSelected(selected: Boolean) {
//        if(selected)
//            listSelectedItm.add(listLargeItm[pos])
//        else
//            listSelectedItm.remove(listLargeItm[pos])

        binding.allSelected.isChecked = listSelectedItm.size == listLargeItm.size
        selectAll = listSelectedItm.size == listLargeItm.size

    }

    override fun beingDeleted(addSize: Boolean, itm: LargeItem) {
        if (addSize) {
            sizeBeingDeleted = sizeBeingDeleted.plus(itm.size)
            listSelectedItm.add(itm)
        }
        else {
            sizeBeingDeleted = sizeBeingDeleted.minus(itm.size)
            listSelectedItm.remove(itm)
        }

        sizeToClean = format(sizeBeingDeleted.toDouble(),2)

        binding.cleanFileSize.text = "Clean Up $sizeToClean"


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

}