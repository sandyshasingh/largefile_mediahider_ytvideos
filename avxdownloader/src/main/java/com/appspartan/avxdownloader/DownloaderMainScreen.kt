package com.appspartan.avxdownloader

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.appspartan.avxdownloader.databinding.DownloaderMainScreenBinding

const val URL_EXTRA = "URL_EXTRA"
const val FB_WATCHLINK = "https://m.facebook.com/watch/"
const val FB_LINK = "https://m.facebook.com"
const val INSTAGRAM_LINK = "https://www.instagram.com"

class DownloaderMainScreen : AppCompatActivity() {

    private lateinit var binding: DownloaderMainScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DownloaderMainScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }




        binding.fbwatch.setOnClickListener {
            resultLauncher.launch(Intent(this,BrowserActivity::class.java).apply {
                putExtra(URL_EXTRA, FB_WATCHLINK)
            })
        }

        binding.facebook.setOnClickListener {
            resultLauncher.launch(Intent(this,BrowserActivity::class.java).apply {
                putExtra(URL_EXTRA, FB_LINK)
            })
        }

        binding.instagram.setOnClickListener {
            resultLauncher.launch(Intent(this,BrowserActivity::class.java).apply {
                putExtra(URL_EXTRA, INSTAGRAM_LINK)
            })
        }

    }

    val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//        val homeFragment = supportFragmentManager.findFragmentById(R.id.video_home_page)
//        if(homeFragment is NewHomePageFragment)
//        {
//            homeFragment.setTextOnEditText(true)
//            homeFragment.refreshRecentDownloads()
//            homeFragment.loadHistory()
//        }
    }

}