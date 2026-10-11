package com.example.largefiles

import android.app.Activity
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.largefiles.databinding.LargeFileItemBinding

class LargeFilesAdapter(
    var mContext: Activity?,
    var listItem: ArrayList<LargeItem>?,
    var allSelected: AllitemSelected,
    var onBeingDeleted: ItemBeingDeleted,
    ) : RecyclerView.Adapter<LargeFilesAdapter.VH>() {

    var selectedIcon: Drawable?=null
    var selectIcon: Drawable?=null
    var itemToLoad: Drawable?=null

    var count =0

    init {




        selectedIcon =  mContext?.let { ResourcesCompat.getDrawable(it.resources, R.drawable.ic_large_selected, null) }
        selectIcon = mContext?.let { ResourcesCompat.getDrawable(it.resources, R.drawable.ic_large_select, null) }


        if (listItem!=null)
            for (i in listItem!!){
                if (i.isSelected)
                    count = count.plus(1)
            }


    }


    inner class VH(val binding: LargeFileItemBinding) : RecyclerView.ViewHolder(binding.root) {
        init {

            binding.root.setOnClickListener {
                listItem?.get(adapterPosition)?.isSelected = !listItem?.get(adapterPosition)?.isSelected!!

                binding.isSelected.isChecked = listItem?.get(adapterPosition)?.isSelected == true

                listItem?.get(adapterPosition)?.let { it1 ->
                    onBeingDeleted.beingDeleted(listItem?.get(adapterPosition)?.isSelected!!,
                        it1
                    )
                }


                count = if(listItem?.get(adapterPosition)?.isSelected == true)
                    count.plus(1)
                else
                    count.minus(1)

                if (listItem!=null){
                    if (count == listItem!!.size)
                        allSelected.onAllSelected(true)
                    else
                        allSelected.onAllSelected(false)
                }



                notifyItemChanged(adapterPosition)
            }
        }



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = LargeFileItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun getItemCount(): Int = listItem?.size?:0

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.binding.pathCleanmaster.text = listItem?.get(position)?.name

        holder.binding.size.text = listItem?.get(position)?.sizeInFormat
        holder.binding.isSelected.isChecked = listItem?.get(position)?.isSelected == true

        Glide.with(holder.binding.iconApp.context)
            .load(listItem?.get(position)?.path)
            .into(holder.binding.iconApp)
            .clearOnDetach()




    }


}