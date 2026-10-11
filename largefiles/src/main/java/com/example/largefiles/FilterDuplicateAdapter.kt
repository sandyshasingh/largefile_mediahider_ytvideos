package com.example.largefiles

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.largefiles.databinding.FilterDuplicateItemsBinding
import java.util.Locale

class FilterDuplicateAdapter(var context: Activity, var list:ArrayList<ArrayList<ListItem>>, private val clickListener: (ArrayList<ListItem>) -> Unit): RecyclerView.Adapter<FilterDuplicateAdapter.HeaderViewHolder>()
{
    private var fileDrawables = HashMap<String, Drawable>()
    private var fileDrawable: Drawable = context.resources.getDrawable(R.drawable.ic_file_generic)

    init {
        fileDrawables = getFilePlaceholderDrawables(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeaderViewHolder {
        val binding = FilterDuplicateItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        val v = LayoutInflater.from(parent.context).inflate(R.layout.filter_duplicate_items, parent, false)
        return HeaderViewHolder(binding)
    }


    override fun onBindViewHolder(holder: HeaderViewHolder, position: Int) {
        holder.bindItems(listItems = list[position] , clickListener)
    }

    override fun getItemCount(): Int {
        Log.d("@Akshay", "getItemCount: AKs${list.size}")
        return list.size?:0
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    inner  class HeaderViewHolder(val binding: FilterDuplicateItemsBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.viewall.setOnClickListener {
                if(adapterPosition >=0 && adapterPosition < list.size) {
                    clickListener(list[adapterPosition])

                }
            }
        }

        fun bindItems(listItems:ArrayList<ListItem>, c1: (ArrayList<ListItem>) -> Unit) {
            binding.duplicateItemCount.text = listItems.size.toString()
            binding.duplicateItemName.text = "Name- " + listItems[0].mName
            binding.duplicateItemSize.text = "Size- " + listItems[0].mSize?.formatSize()
            try {
                val fileName = listItems[0].mName
                val drawable : Drawable = fileDrawables.getOrElse(
                    fileName.substringAfterLast(".").lowercase(
                        Locale.ROOT
                    )
                ) { fileDrawable }

                val options = RequestOptions()
                    .placeholder(drawable)
                    .transform(CenterCrop(), RoundedCorners(dpToPx(16)))

                val itemToLoad = listItems[0].audioImageUri ?:listItems[0].mPath

                if (!context.isDestroyed) {
                    Glide.with(context)
                        .load(itemToLoad)
                        .apply(options)
                        .into(binding.duplicateItemImage)
                }

            }
            catch (e : Exception){
//                FirebaseCrashlytics.getInstance().recordException(e)
            }
        }
    }
    private fun dpToPx(dp: Int): Int {
        return (dp * context.resources.displayMetrics.density).toInt()
    }


}

