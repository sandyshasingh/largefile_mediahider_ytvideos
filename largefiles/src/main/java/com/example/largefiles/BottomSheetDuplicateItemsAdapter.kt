package com.example.largefiles

import android.app.Activity
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.largefiles.databinding.LayoutBottomsheetFilterDuplicateBinding
import java.util.HashMap
import java.util.Locale

class BottomSheetDuplicateItemsAdapter(var context: Activity?, var folders: ArrayList<ListItem>?, var callback:DuplicateItemSelected?) : RecyclerView.Adapter<BottomSheetDuplicateItemsAdapter.VH>()
{
    private var fileDrawables = HashMap<String, Drawable>()
    private lateinit var fileDrawable: Drawable

    init {
        if(context != null)
        {
            fileDrawable = context!!.resources.getDrawable(R.drawable.ic_file_generic)
            fileDrawables = getFilePlaceholderDrawables(context!!)
        }
    }

    inner class VH(val binding: LayoutBottomsheetFilterDuplicateBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            binding.root.setOnClickListener {

                folders?.get(adapterPosition)?.isSelected   = folders?.get(adapterPosition)?.isSelected != true
                binding.duplicateCheckbox.isChecked = folders?.get(adapterPosition)?.isSelected == true
                callback?.duplicateFile(adapterPosition,folders?.get(adapterPosition)?.isSelected == true)

                /* folders?.get(absoluteAdapterPosition)?.path?.let { it1 ->
                     folderClicked?.folderSelected(
                         it1
                     )
                 }*/
            }

        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = LayoutBottomsheetFilterDuplicateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_bottomsheet_filter_duplicate, parent, false)
        return  VH(binding)

    }




    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.binding.duplicateName.text = folders?.get(position)?.mName
        holder.binding.duplicatePath.text = "Path: " + folders?.get(position)?.mPath
        holder.binding.duplicatePath.isSelected = true
        holder.binding.duplicateCheckbox.isChecked = folders?.get(position)?.isSelected == true
        val fileName = folders?.get(position)?.mName?:""
        val drawable : Drawable = fileDrawables.getOrElse(fileName.substringAfterLast(".").toLowerCase(
            Locale.ROOT)) { fileDrawable }

        val options = RequestOptions()
            .placeholder(drawable)
            .transform(CenterCrop(), RoundedCorners(dpToPx(12)))

        val itemToLoad = folders?.get(0)?.audioImageUri ?: folders?.get(0)?.mPath

        if (context != null && context?.isDestroyed == false) {
            Glide.with(context!!)
                .load(itemToLoad)
                .apply(options)
                .into(holder.binding.imageDuplicate)
        }
    }

    private fun dpToPx(dp: Int): Int {
        var tmp = context?.resources?.displayMetrics?.density?.toInt()
        if (tmp != null && tmp>0)
            return (dp * (tmp))

        else return dp*4

    }

    override fun getItemCount(): Int {
        return folders?.size?:0
    }





}
