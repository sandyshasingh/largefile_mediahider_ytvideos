package com.example.ytvideos.adapters


import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ytvideos.databinding.CategoryDataItemBinding
import com.example.ytvideos.dataclasses.YoutubeVideoItem
import com.example.ytvideos.listeners.CategoryclickedListener

class CategoryItemAdapter2(var mContext: Activity,
                          var mList: List<YoutubeVideoItem>?,
                          var listener: CategoryclickedListener
): RecyclerView.Adapter<CategoryItemAdapter2.ViewHolder>() {


    inner class ViewHolder(val binding: CategoryDataItemBinding) : RecyclerView.ViewHolder(binding.root) {
        init {

            binding.videoItem.setOnClickListener {

                if (mList != null)
                    listener.playItem(false,mList?.get(absoluteAdapterPosition)?.id?.videoId!!)

            }
            binding.menu.setOnClickListener {
                listener.playItem(true,mList?.get(absoluteAdapterPosition)?.id?.videoId!!)

            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = CategoryDataItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)

//        val v = LayoutInflater.from(parent.context).inflate(R.layout.notes_home_screen_item, parent, false)
//        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return mList?.size?:0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.videoTitle.text = mList?.get(position)?.snippet?.title
        Log.d("hehaheha", "onBindViewHolder: ${mList?.get(position)}")
        Glide.with(mContext)
            .load(mList?.get(position)?.snippet?.thumbnails?.medium?.url)
//                .apply(requestOptions)
            .into(holder.binding.videoThumbnail)


    }

}