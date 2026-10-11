package com.example.largefiles

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.res.ResourcesCompat
import com.example.largefiles.databinding.BottomsheetFilterDuplicateBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class BottomSheetDuplicateItems2 : BottomSheetDialogFragment(), DuplicateItemSelected,
    CoroutineScope by MainScope() {

    var baseSimpleActivity: Activity? = null
    var listDuplicate:ArrayList<ListItem>? = null
    var callback: ((size:String) -> Unit)? = null
    var listToDelete:ArrayList<ListItem> = ArrayList()
    var selectAll = false
    var selectedItemsList:ArrayList<ListItem> = ArrayList()
    var mainAdapter : BottomSheetDuplicateItemsAdapter? = null
    var totalSizeToDelete:Long = 0L
    private val binding by lazy { BottomsheetFilterDuplicateBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BootomSheetDialogTheme);
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
//        return inflater.inflate(R.layout.bottomsheet_filterduplicate, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        bottomSheetDialog.setOnShowListener {
            /*
            * You can find the bottom sheet parent view and ids:
            * https://github.com/lixw1021/material-components-android/blob/master/lib/java/com/google/android/material/bottomsheet/res/layout/design_bottom_sheet_dialog.xml
            * */

            mainAdapter = BottomSheetDuplicateItemsAdapter(baseSimpleActivity,listDuplicate,this)
            binding.btmSheetFilterduplicateList.adapter = mainAdapter


            val coordinator = (it as BottomSheetDialog)
                .findViewById<CoordinatorLayout>(com.google.android.material.R.id.coordinator)
            val containerLayout =
                it.findViewById<FrameLayout>(com.google.android.material.R.id.container)
            val buttons = bottomSheetDialog.layoutInflater.inflate(R.layout.view_buttons, null)

            buttons.layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.BOTTOM
            }
            containerLayout!!.addView(buttons)

            /*
            * Dynamically update bottom sheet containerLayout bottom margin to buttons view height
            * */
            buttons.post {
                (coordinator!!.layoutParams as ViewGroup.MarginLayoutParams).apply {
                    buttons.measure(
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                    )
                    this.bottomMargin = buttons.measuredHeight
                    containerLayout.requestLayout()
                }
            }
            binding.menuCheckboxSelectAll.setOnClickListener(){
                selectAll = !selectAll
                selectedItemsList.clear()
                if(listDuplicate != null){
                    if(selectAll)
                        selectedItemsList.addAll(listDuplicate!!)
                    else
                        selectedItemsList.removeAll(listDuplicate!!)
                    for(item in listDuplicate!!){
                        item.isSelected = selectAll
                    }
                }
                mainAdapter?.folders = listDuplicate
                mainAdapter?.notifyDataSetChanged()
            }

            buttons.findViewById<View>(R.id.delete_duplicate)?.setOnClickListener()
            {
                if(listDuplicate != null){
                    for(item in listDuplicate!!){
                        if(item.isSelected)
                            listToDelete.add(item.let { ListItem(
                                it.mPath,
                                it.mName,
                                it.mSize,
                                0,
                                null,
                                "",
                                ""

                            ) })
                    }
                }
                if(listToDelete.isEmpty()){
                    Toast.makeText(context,"No file selected", Toast.LENGTH_SHORT).show()
                }
                else{
                    if(baseSimpleActivity != null){

                        ////////
                        Log.d("dikshababy","135 $baseSimpleActivity")
                        if(ThemeUtils.getActivityIsAlive(baseSimpleActivity)){
                            val alertDialog = AlertDialog.Builder(baseSimpleActivity)
                            Log.d("dikshababy","137 $baseSimpleActivity")
                            val view: View? =
                                baseSimpleActivity?.layoutInflater?.inflate(R.layout.delete_confirmation_dialog, null)
                            alertDialog.setView(view)
                            val alert: AlertDialog = alertDialog.create()
                            alert.window?.setBackgroundDrawable(
                                baseSimpleActivity?.resources?.let {
                                    ResourcesCompat.getDrawable(
                                        it,
                                        R.drawable.transparent_drawable,
                                        null
                                    )
                                }
                            )

                            Log.d("dikshababy","150 $view  ${view?.findViewById<TextView?>(R.id.ok_delete)}")

                            view?.findViewById<TextView?>(R.id.ok_delete)?.setOnClickListener {
                                Log.d("dikshababy","154")
                                if (baseSimpleActivity != null) {
                                    deleteSelectedFiles()
                                    alert.dismiss()
                                }
                            }

                            view?.findViewById<TextView?>(R.id.cancel_btn)?.setOnClickListener {
                                Log.d("dikshababy","162 $baseSimpleActivity")
                                if (baseSimpleActivity != null) {
                                    Log.d("dikshababy","164")
                                    alert.dismiss()
                                }
                            }

                            alert.show()
                        }
                        ////////
//                        MoveToTrashDialog(
//                            baseSimpleActivity!!,
//                            baseSimpleActivity?.resources?.getString(R.string.deletion_confirmation)?:""
//                        ) {
//
//                            if (it) {
//
//                                moveToRecycleBin() {
//
//                                }
//                            } else{
//
//
//                            }
//
//                        }
                    }
                    dialog?.dismiss()
                }
            }




        }
        return bottomSheetDialog
    }




    private fun deleteSelectedFiles() {
        launch{
            withContext(Dispatchers.IO) {
                deleteFiles()
                for(itm in listToDelete)
                    totalSizeToDelete += itm.mSize?:0L

                withContext(Dispatchers.Main)
                {
                    if (context!=null) {
                        Toast.makeText(context, "file deleted successfully", Toast.LENGTH_SHORT).show()
                    }
                    callback?.invoke(totalSizeToDelete.formatSize())
                }
            }


        }
    }

    fun deleteFiles() {



        for (i in listToDelete){


                try {

                    val where = "${MediaStore.MediaColumns.DATA} = ?"
                    val args = arrayOf(i.mPath)
                    activity?.contentResolver?.delete(MediaStore.Files.getContentUri("external"), where, args) != 1
                    File(i.mPath).delete()
                }
                catch (e : Exception){
                }


        }
    }

    override fun duplicateFile(pos: Int, delete: Boolean) {
        if(listDuplicate != null){
            if(delete)
                selectedItemsList.add(listDuplicate!![pos])
            else
                selectedItemsList.remove(listDuplicate!![pos])

            binding.menuCheckboxSelectAll.isChecked = selectedItemsList.size == listDuplicate?.size
            selectAll = selectedItemsList.size == listDuplicate?.size
        }
    }
}