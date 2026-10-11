package com.example.largefiles

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.largefiles.databinding.FilterDuplicateFragmentLayoutBinding
import java.util.LinkedHashMap

class FilterDuplicateFragment : Fragment()
{
    var mView: View? = null
    var viewModel : LargeFileViewModel? = null
    var list : ArrayList<ListItem> = ArrayList()
    var listToShow : ArrayList<ArrayList<ListItem>> = ArrayList()
    var mainAdapter: FilterDuplicateAdapter? = null

    private val binding by lazy { FilterDuplicateFragmentLayoutBinding.inflate(layoutInflater) }

    companion object {

        var filterType:Int? = 0

        fun newInstance(name: Int) : FilterDuplicateFragment {
            val myFragment = FilterDuplicateFragment()
            val bundle = Bundle()
            bundle.putInt(PARAM_ID, name)

            myFragment.arguments = bundle
            return myFragment
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        mView = inflater.inflate(R.layout.filter_duplicate_fragment, container, false)
        mView = binding.root


            viewModel = ViewModelProvider(this)[LargeFileViewModel::class.java]

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = arguments
        filterType = bundle?.getInt(PARAM_ID)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backk.setOnClickListener {


            parentFragmentManager.popBackStack()
        }
        Log.d("akshay","81 filter fragment $filterType")
        when(filterType){
            FilterImage ->{
                binding.fragmentTitle.text = "Photos"
                if (context != null)
                    viewModel?.fetchImages(requireContext())

                viewModel?.photos?.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                    if (!it.isNullOrEmpty()|| it.size==0) {
                        list = it as ArrayList<ListItem>


                        listToShow = getDuplicate(list)
                        Log.d("akshay","94 photos ${listToShow.size}")
                        setItems()
                    }
                })
            }
            FilterVideo ->{
                binding.fragmentTitle.text = "Videos"
                if(context!=null)
                    viewModel?.fetchVideos(requireContext())
                viewModel?.videos?.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                    if (!it.isNullOrEmpty()|| it.size==0) {
                        list = it as ArrayList<ListItem>

                        listToShow = getDuplicate(list)

                        setItems()
                    }
                })
            }
            FilterAudio ->{
                binding.fragmentTitle.text = "Audio"
                if (context!=null)
                    viewModel?.fetchAudios(requireContext())
                viewModel?.audios?.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                    if (!it.isNullOrEmpty()|| it.size==0) {
                        list = it as ArrayList<ListItem>

                        listToShow = getDuplicate(list)
                        setItems()
                    }
                })
            }

        }

    }

    private fun setItems() {
        if(listToShow.isNullOrEmpty()){
            Log.d("akshay","145 filterfragment")
            binding.zrpFileFilterDuplicate.visibility = View.VISIBLE
            binding.duplicateItemsList.visibility = View.GONE

        }
        else{
            binding.zrpFileFilterDuplicate.visibility = View.GONE
            binding.duplicateItemsList.visibility = View.VISIBLE

            mainAdapter = FilterDuplicateAdapter(activity as LargeFileMainActivity,
                listToShow,
                { folder -> fixedFolderClick(folder) }
            )
            Log.d("akshay","163 filterfragment ${listToShow.size}  ${mainAdapter} ${binding.duplicateItemsList}")
            binding.duplicateItemsList.adapter = mainAdapter
        }

    }
    fun fixedFolderClick(folder: ArrayList<ListItem>) {

        if(activity != null){
                context?.let {

                    val bottomSheet = BottomSheetDuplicateItems2()
                    bottomSheet.baseSimpleActivity = activity as LargeFileMainActivity
                    bottomSheet.listDuplicate = folder
                    bottomSheet.callback = {

                        showDeleteItemsDialog(it)

                        when (filterType) {
                            FilterImage -> {
                                if (context != null)
                                    viewModel?.fetchImages(requireContext())
                            }
                            FilterVideo -> {
                                if (context != null)
                                    viewModel?.fetchVideos(requireContext())
                            }
                            FilterAudio -> {
                                if (context != null)
                                    viewModel?.fetchAudios(requireContext())
                            }


                        }

                    }
                    activity?.supportFragmentManager?.let { it1 -> bottomSheet.show(it1, "") }

                }

        }

    }

    private fun showDeleteItemsDialog(size:String) {

        if(ThemeUtils.getActivityIsAlive(activity)){
            val alertDialog = AlertDialog.Builder(activity)
            val view: View? =
                activity?.layoutInflater?.inflate(R.layout.duplicate_status_dialog, null)
            alertDialog.setView(view)
            val alert: AlertDialog = alertDialog.create()
            alert.window?.setBackgroundDrawable(
                activity?.resources?.let {
                    ResourcesCompat.getDrawable(
                        it,
                        R.drawable.transparent_drawable,
                        null
                    )
                }
            )
            view?.findViewById<TextView?>(R.id.delete_files_size)?.text = size
//        view?.delete_files_size?.

            view?.findViewById<TextView?>(R.id.ok_filter)?.setOnClickListener()
            {
                if (activity != null) {
                    alert.dismiss()
                }
            }
            alert.show()
        }
    }



    fun getDuplicate(files: ArrayList<ListItem>): ArrayList<ArrayList<ListItem>> {
        val requiredList:ArrayList<ArrayList<ListItem>> = ArrayList()
        val hashSet: LinkedHashMap<Int, ArrayList<ListItem>> = LinkedHashMap<Int, ArrayList<ListItem>>()
        for (commonFile in files) {

            if (hashSet.containsKey(commonFile.fileInfo.hashCode())) {
                hashSet[commonFile.fileInfo.hashCode()]?.add(commonFile)
            } else {
                hashSet[commonFile.fileInfo.hashCode()] = ArrayList<ListItem>().apply {  add(commonFile) }
            }

        }
        for(item in hashSet.keys){
            if ((hashSet[item]?.size?:0) > 1)
                hashSet[item]?.let { requiredList.add(it) }
        }

        return requiredList
    }


}