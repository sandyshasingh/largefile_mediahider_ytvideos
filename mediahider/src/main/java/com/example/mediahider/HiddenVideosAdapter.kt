package com.example.mediahider

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mediahider.databinding.PhotosItemsBinding
import com.example.mediahider.utils.doGone
import com.example.mediahider.utils.doVisible
import com.example.mediahider.utils.loadUri

class HiddenVideosAdapter(var videosList: ArrayList<HiddenFiles>, val context: Context?, var onVideoSelectionListener: OnVideoSelectedListener, var actionModeListener: ActionModeListener?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):  RecyclerView.ViewHolder  {

        val binding = PhotosItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemsViewHolder(binding)

    }

    fun getSelectedItems(): ArrayList<Uri> {
        return ArrayList(
            videosList
                .filter { it.isSelected }
                .mapNotNull { it.originalUri }
        )
//        return photosList.filter { it.isSelected }
    }

    fun dpTopixel(c: Context?, dp: Float): Float {
        val density: Float? = c?.resources?.displayMetrics?.density
        return if(density != null)
            dp * density
        else
            dp
    }


    override fun getItemCount(): Int {
        return if(videosList.isNotEmpty())
            videosList.size
        else 0
    }

    inner class ItemsViewHolder(val binding : PhotosItemsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindItems()
        {
            var postn = adapterPosition
            if((postn < videosList.size) && videosList[postn].isFile!!)
            {
                binding.rlDetails.doGone()
                binding.image.loadUri(videosList[postn].originalUri)
            }
            else
            {
                if(postn < videosList.size){
                    binding.rlDetails.doVisible()
                    binding.image.loadUri(videosList[postn].originalUri)
                    binding.tvFolderName.text = videosList[postn].name
                    Log.d("dikshababy"," size tera ${videosList[postn].size}")
                    binding.tvFolderSize.text = videosList[postn].size
                }

            }
            if((postn < videosList.size) && videosList[postn].isSelected)
            {
//                binding.imgSelected.doVisible()
                binding.imgSelected.setImageResource(R.drawable.ic_select)
                binding.selectionLayer.doVisible()
            }
            else
            {
//                binding.imgSelected.doGone()
                binding.imgSelected.setImageResource(R.drawable.ic_unselect)

                binding.selectionLayer.doGone()
            }
            binding.root.setOnClickListener()
            {
                if((actionModeListener != null && actionModeListener?.getActionMode()!!) || actionModeListener == null)
                {
                    if (postn < videosList.size){
                        if(videosList[postn].isFile!!)
                            selectOrDeselectItems(postn)
                    }

                }
                else
                {
                    if (postn < videosList.size){
                        if(videosList[postn].isFile!!)
                            playFile(postn)
                        else
                            onVideoSelectionListener.onVideoFolderClicked(videosList[postn])
                    }

                }
            }

            binding.imgSelected.setOnClickListener {
                if(actionModeListener !=null && !actionModeListener?.getActionMode()!! && (postn < videosList.size) && (videosList[postn].isFile!!))
                {
                    selectOrDeselectItems(postn)
                    actionModeListener?.setActionModeValue(true)
                    true
                }
                false
            }


        }



        private fun playFile(position : Int) {
            val tempList : ArrayList<HiddenFiles> = ArrayList()
            var minusCount = 0
            for(i in videosList)
            {
                if(i.isFile!!)
                    tempList.add(i)
                else
                    minusCount++
            }
            var finalPosition = position-minusCount
            if(finalPosition < 0) finalPosition = 0
            onVideoSelectionListener.onVideoClicked(tempList,finalPosition)
        }

        private fun selectOrDeselectItems(adapterPosition: Int) {
            if (adapterPosition < videosList.size) {
                if (videosList[adapterPosition].isSelected) {
                    videosList[adapterPosition].isSelected = false
                    onVideoSelectionListener.onVideoDeselected(videosList[adapterPosition])
                } else {
                    videosList[adapterPosition].isSelected = true
                    onVideoSelectionListener.onVideoSelected(videosList[adapterPosition])
                }
                notifyItemChanged(adapterPosition+1)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ItemsViewHolder -> {
                holder.bindItems()
            }
        }

    }

}