package com.example.ytvideos.adapters

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ytvideos.databinding.CircleViewItemsBinding
import com.example.ytvideos.databinding.ItemYoutubeBinding
import com.example.ytvideos.databinding.SubCategoryItemsBinding
import com.example.ytvideos.dataclasses.ListItem
import com.example.ytvideos.listeners.ItemClickedListener

class SubCategoryAdapter(var mContext: Activity,
                         var items: List<ListItem>,
                         var listener: ItemClickedListener,
                         var viewtype:Int,
                         var isYoutubeVideos:Boolean
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_NORMAL = 1
        private const val TYPE_CIRCLE = 2
        private const val TYPE_BIGVIEW = 3
    }

    override fun getItemViewType(position: Int): Int = viewtype


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            TYPE_NORMAL -> YoutubeViewHolder(ItemYoutubeBinding.inflate(inflater, parent, false))
            TYPE_CIRCLE -> SubCategoryCircleViewHolder(CircleViewItemsBinding.inflate(inflater, parent, false))
            else -> SubCategoryViewHolder(SubCategoryItemsBinding.inflate(inflater, parent, false))

        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (viewtype) {
            TYPE_NORMAL ->
                (holder as YoutubeViewHolder)
                    .bind(items[position])

            TYPE_CIRCLE ->
                (holder as SubCategoryCircleViewHolder)
                    .bind(items[position])

            TYPE_BIGVIEW ->
                (holder as SubCategoryViewHolder)
                    .bind(items[position])
        }
    }

    inner class YoutubeViewHolder(val binding: ItemYoutubeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {

            binding.root.setOnClickListener {
                Log.d("diksha ki", "67: ")
                listener.itmClicked(isYoutubeVideos, items[position].title,items[position].url?:"")

            }

        }
        fun bind(item: ListItem) {

            binding.tvTitle.text = item.title

            Glide.with(itemView)
                .load(item.thumbnail)
                .into(binding.ivThumb)
        }
    }

    inner class SubCategoryViewHolder(val binding: SubCategoryItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {

            binding.root.setOnClickListener {
                Log.d("diksha ki", "80: ")
                listener.itmClicked(isYoutubeVideos, items[position].title,items[position].url?:"")


            }

        }
        fun bind(item: ListItem) {
            binding.title.text = item.title

            Glide.with(itemView)
                .load(item.thumbnail)
                .into(binding.childItem)
        }
    }

    inner class SubCategoryCircleViewHolder(val binding: CircleViewItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {

            binding.root.setOnClickListener {
                listener.itmClicked(isYoutubeVideos, items[position].title,items[position].url?:"")


            }

        }

        fun bind(item: ListItem) {
            binding.title.text = item.title

            Glide.with(itemView)
                .load(item.thumbnail)
                .into(binding.childItem)
        }
    }


    override fun getItemCount(): Int {
        return items.size?:0
    }



}