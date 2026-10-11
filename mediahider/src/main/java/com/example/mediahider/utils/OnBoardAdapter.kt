package com.example.mediahider.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.mediahider.AppDetailFragment

class OnBoardAdapter (fm: FragmentManager?) : FragmentStatePagerAdapter(fm!!, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val count = 2

    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> return AppDetailFragment.newInstance(AppDetailFragment.FragmentType.SCREEN1)
            1 -> return AppDetailFragment.newInstance(AppDetailFragment.FragmentType.SCREEN2)

        }
        return AppDetailFragment.newInstance(AppDetailFragment.FragmentType.SCREEN1)
    }

    override fun getCount(): Int {
        return count
    }
}