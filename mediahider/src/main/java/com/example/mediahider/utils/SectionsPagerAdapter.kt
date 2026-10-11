package com.example.mediahider.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.mediahider.fragments.HiddenAudioFragment
import com.example.mediahider.fragments.HiddenPhotoFragment
import com.example.mediahider.fragments.HiddenVideoFragment

class SectionsPagerAdapter(fragmentActivity: FragmentActivity,val videoPath: String, val photoPath: String, val musicPath: String,
                           private val onUploadClickListenerForCamera: onUploadClickListener
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 3



    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HiddenPhotoFragment.newInstance(photoPath,onUploadClickListenerForCamera)
            1 -> HiddenVideoFragment.newInstance(videoPath,onUploadClickListenerForCamera)
            2 -> HiddenAudioFragment.newInstance(musicPath,onUploadClickListenerForCamera)
            else -> throw IllegalArgumentException("Invalid position $position")
        }
//        return PlaceholderFragment.newInstance(position, onUploadClickListenerForCamera)
    }


}