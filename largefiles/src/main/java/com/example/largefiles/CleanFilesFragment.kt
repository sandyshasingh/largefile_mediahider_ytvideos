package com.example.largefiles

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.largefiles.databinding.CleanFileFragmentLayoutBinding

class CleanFilesFragment : Fragment() {

    var sizeCleaned:String = ""

    private val binding by lazy {  CleanFileFragmentLayoutBinding.inflate(layoutInflater) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return binding.root
//        return inflater.inflate(R.layout.fragment_clean_two, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.sizeCleaned.text = "$sizeCleaned Freed!"


        binding.backHome.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }

        binding.finalBackk.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }


    }

}