package com.example.cleanfiles

import android.app.Activity

import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.cleanfiles.databinding.ChildItemBinding


class ChildCleanmasterAdapter(
    var mContext: Activity?,
    var listItem: ArrayList<ApkItem>?,
    var allSelected: AllitemSelected,
    var pos: Int,
    var onBeingDeleted: ItemBeingDeleted,
    var isDirc: Boolean
)  : RecyclerView.Adapter<ChildCleanmasterAdapter.VH>() {

   private var requestForDir:RequestOptions?=null
   private var requestForNonDir:RequestOptions?=null
    var selectedIcon:Drawable?=null
    var selectIcon:Drawable?=null
    var itemToLoad:Drawable?=null

    var count =0

  init {

//      if(pos == 1){
//          itemToLoad = mContext?.let { ResourcesCompat.getDrawable(it.resources, R.drawable.apk_icon, null) }
//      }
      itemToLoad = when (pos){
          1-> mContext?.let { ResourcesCompat.getDrawable(it.resources, R.drawable.ic_wer, null) }
          2-> mContext?.let { ResourcesCompat.getDrawable(it.resources, R.drawable.ic_folder, null) }
          else -> mContext?.let { ResourcesCompat.getDrawable(it.resources, R.drawable.apk_icon, null) }
      }

//       requestForDir = RequestOptions().placeholder(R.drawable.ic_folder)
//       requestForNonDir = RequestOptions().placeholder(R.drawable.ic_wer)


      selectedIcon =  mContext?.let { ResourcesCompat.getDrawable(it.resources, R.drawable.ic_clean_selected, null) }
      selectIcon = mContext?.let { ResourcesCompat.getDrawable(it.resources, R.drawable.ic_clean_select, null) }


      if (listItem!=null)
        for (i in listItem!!){
            if (i.isSelected)
                count = count.plus(1)
        }


  }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {

        val binding = ChildItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
//        return VH(
//            LayoutInflater.from(parent.context).inflate(R.layout.child_item, parent, false)
//        )
    }

    override fun getItemCount(): Int = listItem?.size?:0

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.binding.pathCleanmaster.text = listItem?.get(position)?.name

        holder.binding.size.text = listItem?.get(position)?.sizeInFormat



        if (listItem?.get(position)?.isSelected == true)
            holder.binding.isSelected.setImageDrawable(selectedIcon)
        else
            holder.binding.isSelected.setImageDrawable(selectIcon)

//        val requestOptions = if (isDirc) requestForDir  else requestForNonDir


//        if (requestOptions != null) {
            Glide.with(holder.binding.iconApp.context)
                .load(itemToLoad)
//                .apply(requestOptions)
                .into(holder.binding.iconApp)
                .clearOnDetach()
//        }



    }

    inner class VH(val binding: ChildItemBinding) : RecyclerView.ViewHolder(binding.root) {
        init {

            binding.root.setOnClickListener {
                listItem?.get(adapterPosition)?.isSelected = !listItem?.get(adapterPosition)?.isSelected!!

                listItem?.get(adapterPosition)?.size?.let { it1 ->
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
                        allSelected.onAllSelected(true,pos)
                    else
                        allSelected.onAllSelected(false,pos)
                }



                notifyItemChanged(adapterPosition)
            }
        }



    }
}