package com.example.mediahider.utils//package com.appspartan.musicplayer
//
//import android.content.ContentValues
//import android.content.Context
//import android.media.MediaPlayer
//import android.net.Uri
//import android.os.Build
//import android.provider.MediaStore
//import android.util.Log
//import android.widget.Toast
//import androidx.activity.result.IntentSenderRequest
//import androidx.annotation.RequiresApi
//import androidx.core.content.ContextCompat
////import com.appspartan.common.data.Audio
////import com.appspartan.compose_ui.base.BaseComposeComponentActivity
////import com.appspartan.core.extention.toast
////import com.appspartan.core.utils.AppSharedPreferencesUtils
////import com.appspartan.musicplayer.media.constants.MusicConstants
////import com.appspartan.musicplayer.presentation.viewModel.AudioMusicMediaViewModel
////import com.appspartan.musicplayer.presentation.viewModel.MusicViewModel
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//import java.io.File
//
//object MusicUtils {
//
//    object MusicNavigationSource {
//        const val MUSIC_FOLDER = "music_folder"
//        const val RECENT = "recent"
//        const val HISTORY = "history"
////        const val VIDEO = "video"
//        const val ALL_MUSIC = "all_music"
////        const val ALL_PASSED_VIDEOS = "all_passed_videos"
//    }
//
//    object ForYouMusicTypeTag {
//        const val FRESH_ARRIVAL = "Fresh Arrival"
//        const val RECENT = "recent"
//    }
//    object MusicTabsTag {
//        const val TAB_ALL_SONGS = "All Songs"
//        const val TAB_FOR_YOU = "For You"
//        const val TAB_MUSIC_FOLDER = "Folders"
//        const val TAB_PLAYLISTS = "Playlists"
//        const val TAB_ARTIST = "Artist"
//        const val TAB_ALBUMS = "Albums"
//        const val TAB_GENRES = "Genres"
//
//    }
//
//
//    var mediaPlayer: MediaPlayer? = null
//     fun createMediaPlayer() {
//        if(mediaPlayer == null){
//            mediaPlayer = MediaPlayer()
//        }
//
//    }
//
//
//    fun clearPreference(){
//        CoroutineScope(Dispatchers.IO).launch {
//            AppSharedPreferencesUtils.putString(MusicConstants.SELECTED_FOLDER_NAME_PREFS_KEY, "")
//            AppSharedPreferencesUtils.putString(MusicConstants.SELECTED_FOLDER_ID_PREFS_KEY, "")
//            AppSharedPreferencesUtils.putLong(MusicConstants.SELECTED_AUDIO_ID_PREFS_KEY, -1)
//        }
//    }
//
//
//    @RequiresApi(Build.VERSION_CODES.R)
//    fun requestDeleteAudioFolder(
//        context: Context,
//        viewModel: AudioMusicMediaViewModel,
//        isFolderClicked: Boolean,
//    ) {
//        val uris = ArrayList<Uri>()
//        val file = File(viewModel.selectedFolder.path)
//
//        if (file.exists()) {
//            val audioFiles = file.listFiles { f -> f.isFile } ?: emptyArray()
//            for (audioFile in audioFiles) {
//                val uri = getAudioContentUri(context, audioFile)
//                if (uri != null) {
//                    uris.add(uri)
//                }
//            }
//        }
//
//        if (uris.isEmpty()) {
//            Toast.makeText(context, ContextCompat.getString(context, com.appspartan.core.R.string.no_audio_files_found), Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        val fileActivity = context as BaseComposeComponentActivity
//        fileActivity.setActivityResultCallbackListener { isSuccess ->
//            if (isSuccess) {
//                viewModel.removeMusicFolder(fileActivity, viewModel.selectedFolder)
//                Log.d("DeleteHandle", "deleteResult success: $isSuccess")
//            } else {
//                Log.d("DeleteHandle", "deleteResult failure: $isSuccess")
//            }
//            context.contentResolver.notifyChange(Uri.parse("content://media"), null)
//        }
//
//        try {
//            val pendingIntentSender = MediaStore.createDeleteRequest(
//                context.contentResolver,
//                uris
//            ).intentSender
//
//            fileActivity.activityResultLauncher.launch(
//                IntentSenderRequest.Builder(pendingIntentSender).build()
//            )
//        } catch (e: Exception) {
//            Log.e("DeleteHandle", "Failed to create delete request", e)
//            Toast.makeText(context, "Error deleting audio files", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//
//    fun deleteMusicFolderBelowR(
//        context: Context,
//        viewModel: AudioMusicMediaViewModel,
//        setShowDialog: (Boolean) -> Unit,
//        isFolderClicked: Boolean,
//    ) {
//        CoroutineScope(Dispatchers.Default).launch {
//            try {
////                if (isFolderClicked) {
//                    val file = File(viewModel.selectedFolder.path)
//                    if (file != null && file.exists()) {
//                        val videoFiles = file.listFiles { file ->
//                            file.delete()
//                        }
//                        viewModel.removeMusicFolder(context,viewModel.selectedFolder)
//                    }
////                } else {
////                    val file = File(viewModel.selectedVideoForMoreCTABottomSheet.path)
////                    if (file != null && file.exists()) {
////                        file.delete()
////                        viewModel.removeVideoItem(context)
////                    }
////                }
//            } catch (ex: Exception) {
//                context.toast(ContextCompat.getString(context,com.appspartan.core.R.string.something_went_wrong_msg))
//            }
//            withContext(Dispatchers.Main) {
//                setShowDialog(false)
//            }
//        }
//    }
//
//    fun getAudioContentUri(context: Context, file: File): Uri? {
//        return try {
//            val filePath = file.absolutePath
//            val cursor = context.contentResolver.query(
//                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, arrayOf(MediaStore.Audio.AudioColumns._ID),
//                MediaStore.Audio.AudioColumns.DATA + "=? ", arrayOf(filePath),
//                null
//            )
//            if (cursor != null && cursor.moveToFirst()) {
//
//                val idColNum =
//                    cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns._ID)
//
//                val id = cursor.getLong(idColNum)
//
//
//                //val id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID))
//                cursor.close()
//                Uri.withAppendedPath(
//                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
//                    "" + id
//                )
//            } else {
//                if (file.exists()) {
//                    val values = ContentValues()
//                    values.put(MediaStore.Audio.AudioColumns.DATA, filePath)
//                    context.contentResolver.insert(
//                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, values
//                    )
//                } else {
//
//                    Toast.makeText(context,  ContextCompat.getString(context,com.appspartan.core.R.string.file_can_not_be_deleted), Toast.LENGTH_SHORT).show()
//                    null
//                }
//            }
//        } catch (e: Exception) {
//            Toast.makeText(context,  ContextCompat.getString(context,com.appspartan.core.R.string.file_can_not_be_deleted), Toast.LENGTH_SHORT).show()
//            null
//        }
//    }
//
//
//   // private var audioListToDelete = emptyList<Audio>()
//    /**
//     * It  creates delete request from MediaStore.createDeleteRequest
//     */
//    @RequiresApi(Build.VERSION_CODES.R)
//    fun requestDeleteAudio(
//        context: Context,
//        audioListToDelete: List<Audio>,
//        musicViewModel: MusicViewModel,
//        musicNavigationSource: String = MusicUtils.MusicNavigationSource.ALL_MUSIC
//    ) {
//      //  this.audioListToDelete = audioListToDelete
//        val uris = ArrayList<Uri>()
//
//      CoroutineScope(Dispatchers.Default).launch {
//                audioListToDelete.map {
//                    uris.add(it.uri)
//                }
//
//            withContext(Dispatchers.Main) {
//                val fileActivity = context as BaseComposeComponentActivity
//                fileActivity.setActivityResultCallbackListener { isSuccess ->
//                    if (isSuccess) {
//                        musicViewModel.removeMusicFilesFromList(audioListToDelete,musicNavigationSource)
//                        // Handle success
//                    } else {
//                        // Handle failure
//                        Log.d("DeleteHandle", "deleteResult failure" + isSuccess)
//                    }
//                    // viewModel.isShowFolderMoreCtsBottomSheet = false
//                    context.getContentResolver().notifyChange(Uri.parse("content://media"), null)
//                }
//
//                val pendingIntentSender = MediaStore.createDeleteRequest(
//                    context.contentResolver,
//                    uris
//                ).intentSender
//                pendingIntentSender?.let { sender ->
//                    (context as BaseComposeComponentActivity).activityResultLauncher.launch(
//                        IntentSenderRequest.Builder(
//                            sender
//                        ).build()
//                    )
//                }
//            }
//        }
//
//    }
//
//}