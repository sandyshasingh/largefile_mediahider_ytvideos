package com.example.cleanfiles

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.cleanfiles.databinding.ChildRecyclerViewBinding
//import kotlinx.android.synthetic.main.child_recycler_view.view.*


class ParentCleanmasterAdapter(
    var mContext: Activity?,
    var listItem: ArrayList<CleanmasterItems>?,
    var onBeingDeleted: ItemBeingDeleted,
    ): RecyclerView.Adapter<ParentCleanmasterAdapter.ViewHolder>(),AllitemSelected {

    var childAdapter : ChildCleanmasterAdapter? = null
    var isExpanded = false
    var sizeBeingDeleted = 0L


    inner class ViewHolder(val binding: ChildRecyclerViewBinding) : RecyclerView.ViewHolder(binding.root) {



        init {
            binding.isSelectedCleanmaster.setOnClickListener {
                if (listItem?.get(adapterPosition)?.allSelected != null){
                    listItem?.get(adapterPosition)?.allSelected = !listItem?.get(adapterPosition)?.allSelected!!
                    if (listItem?.get(adapterPosition)?.list != null){
                        sizeBeingDeleted = 0
                        for (i in listItem?.get(adapterPosition)?.list!!){
                            i.isSelected = listItem?.get(adapterPosition)?.allSelected!!
                            sizeBeingDeleted = sizeBeingDeleted.plus(i.size)

                        }

                        onBeingDeleted.beingDeleted(listItem?.get(adapterPosition)?.allSelected!!,sizeBeingDeleted)
                    }

                    notifyItemChanged(adapterPosition)
                }
            }
            binding.expand.setOnClickListener {


                isExpanded = !isExpanded
              //  parent_recycler_view_clean_master?.findViewHolderForAdapterPosition(adapterPosition)?.itemView?.child_recycler_view_clean_master?.visibility = View.VISIBLE
                notifyItemChanged(adapterPosition)
            }
            binding.collapse.setOnClickListener {
                isExpanded = false
              //  parent_recycler_view_clean_master?.findViewHolderForAdapterPosition(adapterPosition)?.itemView?.child_recycler_view_clean_master?.visibility = View.GONE

                notifyItemChanged(adapterPosition)
            }
        }
    }
    fun format(bytes: Double, digits: Int): String {

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ChildRecyclerViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        holder.binding.titleCleanmaster.text = listItem?.get(position)?.name
        holder.binding.categoryIcon.setImageDrawable(mContext?.let { listItem?.get(position)?.icon?.let { it1 ->
            ResourcesCompat.getDrawable(it.resources,
                it1, null)
        } })
        var size = listItem?.get(position)?.size?.toDouble()?.let { format(it, 2) }
        if (size?.startsWith("0.00") == true){
            holder.binding.sizeCleanmaster.text = mContext?.getString(R.string.no_junk) ?: "No junk"
        }
        else{
            holder.binding.sizeCleanmaster.text = size
        }
        holder.binding.countItems.text = (listItem?.get(position)?.list?.size?:0).toString() + " Items"
        if (listItem?.get(position)?.allSelected == true)
            holder.binding.isSelectedCleanmaster.setImageDrawable(mContext?.let { ResourcesCompat.getDrawable(it.resources, R.drawable.ic_clean_selectall_yes, null) })
        else
            holder.binding.isSelectedCleanmaster.setImageDrawable(mContext?.let { ResourcesCompat.getDrawable(it.resources, R.drawable.ic_clean_selectall_no, null) })

        if (!isExpanded){
            holder.binding.childRecyclerViewCleanMaster.visibility = View.GONE
            holder.binding.expand.visibility = View.VISIBLE

            holder.binding.collapse.visibility = View.GONE

        }
        else{
            holder.binding.childRecyclerViewCleanMaster.visibility = View.VISIBLE
            holder.binding.expand.visibility = View.GONE
            holder.binding.collapse.visibility = View.VISIBLE
        }

        if (listItem?.get(position)?.list?.isNotEmpty() == true){
            holder.binding.loader.visibility = View.GONE
            holder.binding.sizeCleanmaster.visibility = View.VISIBLE
//            holder.itemView.is_selected_cleanmaster.visibility = View.VISIBLE

        }
        if (listItem?.get(position)?.isFetched == true){
            holder.binding.loader.visibility = View.GONE

            holder.binding.sizeCleanmaster.visibility = View.VISIBLE

        }


        childAdapter = ChildCleanmasterAdapter(
            mContext,
            listItem?.get(position)?.list,
            this,
            holder.adapterPosition,
            onBeingDeleted,
            listItem?.get(position)?.isDir == true
        )
        holder.binding.childRecyclerViewCleanMaster.adapter = childAdapter
    }


    override fun getItemCount(): Int {
       return listItem?.size?:0
    }

    override fun onAllSelected(selected: Boolean, pos: Int) {
        listItem?.get(pos)?.allSelected = selected
        notifyItemChanged(pos)

    }

}