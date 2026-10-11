package com.example.mediahider.utils

import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2

fun ViewPager2.onPageChangeListener(pageChangedAction: (newPosition: Int) -> Unit) :
    ViewPager2.OnPageChangeCallback {

        val callback = object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                pageChangedAction(position)
            }
        }

        registerOnPageChangeCallback(callback)
        return callback
    }

fun ViewPager.onPageChangeListener(pageChangedAction: (newPosition: Int) -> Unit) =
    addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {}

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

        override fun onPageSelected(position: Int) {
            pageChangedAction(position)
        }
    })


