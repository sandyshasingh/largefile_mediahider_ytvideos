package com.example.mediahider.utils

import android.net.Uri
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.Glide
import com.example.mediahider.R


fun View.doVisible() {
    if (!isVisible())
        visibility = View.VISIBLE
}

fun View.doGone() {
    if (isVisible())
        visibility = View.GONE
}

fun View.isVisible() : Boolean
{
    return visibility == View.VISIBLE
}

fun ImageView.loadUri(uri: Uri?){
    val requestOptions = RequestOptions().placeholder(R.drawable.mediahide_permisson_zrp)
    Glide.with(this.context).asBitmap().optionalCenterCrop()
        .load(uri).thumbnail(0.04f).apply(requestOptions)
        .into(this)
}