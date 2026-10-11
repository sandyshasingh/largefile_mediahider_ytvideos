package com.example.ytvideos.listeners

import com.example.ytvideos.HomeUiItem
import com.example.ytvideos.dataclasses.ListItem

interface CategoryclickedListener {
    fun categoryItem(categoryList:List<ListItem>)
    fun playItem(isShare:Boolean,url:String)


}