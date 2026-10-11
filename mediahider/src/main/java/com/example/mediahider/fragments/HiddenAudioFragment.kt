package com.example.mediahider.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mediahider.R
import com.example.mediahider.utils.onUploadClickListener


class HiddenAudioFragment : Fragment() {

    var onUploadListener: onUploadClickListener? = null
    var musicPath = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hidden_audio, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(musicPath: String, onUploadClickListener: onUploadClickListener) =
            HiddenAudioFragment().apply {
//                arguments = Bundle().apply {
//                    putInt(PAGER_POSITION, param1)
//                }
                this.musicPath = musicPath
                onUploadListener = onUploadClickListener
            }
    }
}