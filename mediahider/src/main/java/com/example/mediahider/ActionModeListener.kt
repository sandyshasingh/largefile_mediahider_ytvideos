package com.example.mediahider

interface ActionModeListener {
    fun getActionMode(): Boolean?
    fun setActionModeValue(actionMode : Boolean)
    fun onSelectionCountChanged(selectedCount: Int)

}
