package com.example.largefiles

import android.content.Context
import android.graphics.drawable.Drawable
import java.text.DecimalFormat
import java.util.HashMap

const val PHOTOS_ID = 1
const val VIDEOS_ID = 2
const val AUDIO_ID = 3

const val FilterImage = 198
const val FilterVideo = 199
const val FilterAudio = 200

fun Long.formatSize(): String {
    if (this <= 0) {
        return "0 B"
    }

    val units = arrayOf("B", "kB", "MB", "GB", "TB")
    val digitGroups = (Math.log10(toDouble()) / Math.log10(1024.0)).toInt()
    return "${DecimalFormat("#,##0.#").format(this / Math.pow(1024.0, digitGroups.toDouble()))} ${units[digitGroups]}"
}

fun getFilePlaceholderDrawables(context: Context): HashMap<String, Drawable> {
    val fileDrawables = HashMap<String, Drawable>()
    try{

        hashMapOf<String, Int>().apply {
            put("ai", R.drawable.photo_placeholder)
            put("bmp", R.drawable.photo_placeholder)
            put("webp", R.drawable.photo_placeholder)
            put("avi", R.drawable.video_placeholder1)

            put("fla", R.drawable.video_placeholder1)
            put("flv", R.drawable.video_placeholder1)

            put("jpg", R.drawable.photo_placeholder)
            put("jpeg", R.drawable.photo_placeholder)

            put("m4a", R.drawable.music_placeholder)
            put("mp3", R.drawable.music_placeholder)
            put("aac", R.drawable.music_placeholder)
            put("wma", R.drawable.music_placeholder)
            put("mp4", R.drawable.video_placeholder1)
            put("ogg", R.drawable.music_placeholder)

            put("wav", R.drawable.music_placeholder)
            put("flac", R.drawable.music_placeholder)
            put("opus", R.drawable.music_placeholder)
            put("wmv", R.drawable.video_placeholder1)

            put("webm", R.drawable.video_placeholder1)
            put("3gp", R.drawable.video_placeholder1)
            put("3gpp", R.drawable.video_placeholder1)
            put("mov", R.drawable.video_placeholder1)
            put("m4v", R.drawable.video_placeholder1)


        }.forEach { (key, value) ->
            fileDrawables[key] = context.resources.getDrawable(value)
        }
        return fileDrawables
    }catch (e:Exception){
    }
    return fileDrawables
}