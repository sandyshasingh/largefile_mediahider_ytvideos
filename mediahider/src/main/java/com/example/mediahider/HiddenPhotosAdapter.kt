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

interface OnSelectionChangedListener {
    fun onSelectionChanged(selectedItems: List<HiddenFiles>)
}

class HiddenPhotosAdapter (var photosList: ArrayList<HiddenFiles>, val context: Context?, var onImageSelectedListener: OnImageSelectionListener, var actionModeListener: ActionModeListener?, private val selectionListener: OnSelectionChangedListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val selectedItems = mutableListOf<HiddenFiles>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):  RecyclerView.ViewHolder  {

        val binding = PhotosItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemsViewHolder(binding)

    }

    fun getSelectedItems(): ArrayList<Uri> {
        return ArrayList(
            photosList
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
        return if(photosList.isNotEmpty())
            photosList.size
        else 0
    }

    inner class ItemsViewHolder(val binding : PhotosItemsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindItems()
        {
            var postn = adapterPosition
            val file = photosList[postn]
//            if((postn < photosList.size) && photosList[postn].isFile!!)
//            {
//                binding.rlDetails.doGone()
//                binding.image.loadUri(photosList[postn].originalUri)
//            }
//            else
//            {
//                if(postn < photosList.size){
                    binding.rlDetails.doVisible()
                    binding.image.loadUri(file.originalUri)
                    binding.tvFolderName.text = file.name
                    Log.d("dikshababy"," size tera ${file.size}")
                    binding.tvFolderSize.text = file.size
                    binding.itemCheckBox.setOnCheckedChangeListener(null)

                    binding.itemCheckBox.isChecked = file.isSelected
                    binding.itemCheckBox.setOnCheckedChangeListener { _, isChecked ->
                        photosList[postn].isSelected = isChecked

                        if (isChecked) {
                            selectedItems.add(file)
                        } else {
                            selectedItems.remove(file)
                        }
                        selectionListener.onSelectionChanged(selectedItems)
                    }

//                }

//            }
            if((postn < photosList.size) && photosList[postn].isSelected)
            {
//                binding.imgSelected.doVisible()
//                binding.imgSelected.setImageResource(R.drawable.ic_select)
                binding.selectionLayer.doVisible()
            }
            else
            {
//                binding.imgSelected.doGone()
//                binding.imgSelected.setImageResource(R.drawable.ic_unselect)

                binding.selectionLayer.doGone()
            }
//            binding.root.setOnClickListener()
//            {
//                Log.d("dikshababy","77")
//                if((actionModeListener != null && actionModeListener?.getActionMode()!!) || actionModeListener == null)
//                {
//                    Log.d("dikshababy","80")
//
//                    if (postn < photosList.size){
//                        if(photosList[postn].isFile!!) {
//                            Log.d("dikshababy","84")
//
//                            selectOrDeselectItems(postn)
//                        }
//                    }
//
//                }
//                else
//                {
//                    if (postn < photosList.size){
//                        if(photosList[postn].isFile!!) {
//                            playFile(postn)
//                            Log.d("dikshababy","96")
//
//                        }
//                        else {
//                            Log.d("dikshababy","98")
//
//                            onImageSelectedListener.onImageFolderClicked(photosList[postn])
//                        }
//                    }
//
//                }
//            }

//            binding.imgSelected.setOnClickListener {
//                Log.d("dikshababy","110")
//
//                if (postn < photosList.size && photosList[postn].isFile == true) {
//                    Log.d("dikshababy","113")
//
//                    selectOrDeselectItems(postn)
//                    actionModeListener?.setActionModeValue(true)
//                }
//            }


        }



        private fun playFile(position : Int) {
            val tempList : ArrayList<HiddenFiles> = ArrayList()
            var minusCount = 0
            for(i in photosList)
            {
                if(i.isFile!!)
                    tempList.add(i)
                else
                    minusCount++
            }
            var finalPosition = position-minusCount
            if(finalPosition < 0) finalPosition = 0
            onImageSelectedListener.onImageClicked(tempList,finalPosition)
        }

        private fun selectOrDeselectItems(adapterPosition: Int) {
            if (adapterPosition < photosList.size) {
                val item = photosList[adapterPosition]
                item.isSelected = !item.isSelected

                if (item.isSelected)
                    onImageSelectedListener.onImageSelected(item)
                else
                    onImageSelectedListener.onImageDeselected(item)

                notifyItemChanged(adapterPosition)
                actionModeListener?.onSelectionCountChanged(getSelectedItems().size)

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

