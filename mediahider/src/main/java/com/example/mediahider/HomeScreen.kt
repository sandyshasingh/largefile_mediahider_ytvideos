package com.example.mediahider

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mediahider.databinding.ActivityHomeScreenBinding
import com.example.mediahider.utils.SectionsPagerAdapter
import com.example.mediahider.utils.StorageHelper
import com.example.mediahider.utils.StorageUtils
import com.example.mediahider.utils.onUploadClickListener
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class HomeScreen : AppCompatActivity(), onUploadClickListener {

    private lateinit var binding:ActivityHomeScreenBinding
    private var pagerAdapter: SectionsPagerAdapter? = null
    var videoPath = ""
    var photoPath = ""
    var musicPath = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val tabIcons = listOf(R.drawable.ic_photos, R.drawable.ic_videos, R.drawable.ic_audios)
        val tabTitles = listOf("Photos", "Videos", "Audio")


        videoPath = if (StorageHelper.isR()) {
            StorageUtils.getPrivateVideoStorageDirR().path
        } else {
            StorageUtils.getPrivateVideoStorageDir(this).path
        }
        photoPath = if (StorageHelper.isR()) {
            StorageUtils.getPrivatePhotoStorageDirR().path
        } else {
            StorageUtils.getPrivatePhotoStorageDir(this).path
        }
        musicPath = if (StorageHelper.isR()) {
            StorageUtils.getPrivateMusicStorageDirR().path
        } else {
            StorageUtils.getPrivateMusicStorageDir(this).path
        }



        binding.tabs.tabGravity = TabLayout.GRAVITY_START
        pagerAdapter = SectionsPagerAdapter(this,videoPath,photoPath,musicPath,this)
        binding.viewPager.adapter = pagerAdapter

        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            val tabView = LayoutInflater.from(this).inflate(R.layout.custom_tab, null)
            val icon = tabView.findViewById<ImageView>(R.id.tabIcon)
            val text = tabView.findViewById<TextView>(R.id.tabText)

            icon.setImageResource(tabIcons[position])
            text.text = tabTitles[position]

            tab.customView = tabView
        }.attach()

        binding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.customView?.findViewById<LinearLayout>(R.id.tabContainer)
                    ?.setBackgroundResource(R.drawable.tab_selected_bg)

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.customView?.findViewById<LinearLayout>(R.id.tabContainer)
                    ?.setBackgroundResource(R.drawable.tab_unselected_bg)

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

    }

    override fun onUploadPhotoClicked(path: String) {
        TODO("Not yet implemented")
    }

    override fun onUploadVideoClicked(path: String) {
        TODO("Not yet implemented")
    }

    override fun onUploadAudioClicked(path: String) {
        TODO("Not yet implemented")
    }
}