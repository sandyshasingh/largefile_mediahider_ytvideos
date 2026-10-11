package com.example.mediahider

interface OnImageSelectionListener {
    fun onImageDeselected(hiddenPhotos: HiddenFiles)
    fun onImageSelected(hiddenPhotos: HiddenFiles)
    fun onImageClicked(hiddenPhotos: List<HiddenFiles>, position: Int)
    fun onImageFolderClicked(hiddenPhotos: HiddenFiles)
}
