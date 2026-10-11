package com.example.cleanfiles

import android.content.Context
import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.cleanfiles.databinding.DeletedItemViewBinding
//import kotlinx.android.synthetic.main.child_item.view.*
//import kotlinx.android.synthetic.main.deleted_item_view.view.*
import java.io.File

class DeletedItemsListAdapter(var mContext: Context?, var listItem:ArrayList<ApkItem>?)  : RecyclerView.Adapter<DeletedItemsListAdapter.VH>() {

    private var items = listOf<String>()

    var count =0




    fun setItems(list: List<String>) {
        this.items = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {

        val binding = DeletedItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
//        return VH(
//            LayoutInflater.from(parent.context).inflate(
//                R.layout.deleted_item_view,
//                parent,
//                false
//            )
//        )
    }

    override fun getItemCount(): Int = listItem?.size?:0

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.binding.pathDeleteditem.text = listItem?.get(position)?.name
        var size = listItem?.get(position)?.size?.let { format(it.toDouble(), 2) }
        holder.binding.sizeDeleteditem.text = size

    }

    fun format(bytes: Double, digits: Int): String {

//        var size = format(fileList[i].length().toDouble(), 2)
        var byte = bytes
        val dictionary = arrayOf("bytes", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB")
        var index = 0
        while (index < dictionary.size) {
            if (byte < 1024) {
                break
            }
            byte /= 1024
            index++
        }
        return String.format("%." + digits + "f", byte) + " " + dictionary[index]
    }
    inner class VH(val binding: DeletedItemViewBinding) : RecyclerView.ViewHolder(binding.root) {

    }
}