package com.example.ytvideos

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider

import com.example.ytvideos.adapters.CategoryItemAdapter
import com.example.ytvideos.adapters.CategoryItemAdapter2
import com.example.ytvideos.databinding.ActivityCategoryDataBinding
import com.example.ytvideos.dataclasses.ListItem
import com.example.ytvideos.listeners.CategoryclickedListener
import com.example.ytvideos.retrofit.ApiRepository
import com.example.ytvideos.retrofit.ApiResult
import com.example.ytvideos.retrofit.AuthViewModel
import com.example.ytvideos.retrofit.AuthViewModelFactory
import com.example.ytvideos.retrofit.RetrofitClient

class CategoryData : AppCompatActivity(), CategoryclickedListener {
    private lateinit var binding:ActivityCategoryDataBinding
    private lateinit var viewModel: AuthViewModel  // <-- declare here
    var categoryItemAdapter: CategoryItemAdapter? = null
    var categoryItemAdapter2: CategoryItemAdapter2? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCategoryDataBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val items =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableArrayListExtra(
                    "HOME_ITEMS",
                    ListItem::class.java
                )
            } else {
                @Suppress("DEPRECATION")
                intent.getParcelableArrayListExtra("HOME_ITEMS")
            } ?: emptyList()

        var isLoadData = intent.getBooleanExtra("LOAD_DATA",false)
        var categoryName = intent.getStringExtra("ITEM_TITLE")

        val videos = items  // <-- List<YoutubeVideoItem>

        val repository = ApiRepository(RetrofitClient.apiService)
        val factory = AuthViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[AuthViewModel::class.java]


        if (isLoadData){
            viewModel.getYoutubeData(categoryName!!)
        }
        else{
            categoryItemAdapter = CategoryItemAdapter(
                this@CategoryData,
                videos,
                this@CategoryData,
            )
            binding.rvCategory.adapter = categoryItemAdapter
        }


        viewModel.apiResponse.observe(this) { result ->
            when (result) {
                is ApiResult.Loading -> {
                    // show progress bar
                }
                is ApiResult.Success -> {

                    val list = result.data.items  // <-- List<YoutubeVideoItem>

                    categoryItemAdapter2 = CategoryItemAdapter2(
                        this@CategoryData,
                        list,
                        this@CategoryData,
                    )
                    binding.rvCategory.adapter = categoryItemAdapter2
                    Toast.makeText(this, "✅ $result", Toast.LENGTH_SHORT).show()

                }
                is ApiResult.Error -> {
                    Toast.makeText(this, "❌ $result", Toast.LENGTH_SHORT).show()

                }
            }
        }
    }

//    override fun categoryItem(categoryName: String) {
//        // from here we should open youtube or the player to play the video
//    }

    override fun categoryItem(categoryList: List<ListItem>) {

    }

    override fun playItem(isShare:Boolean, url: String) {
        if(isShare){
            shareUrl(url)
        }
        else
            openYoutubeLink(url)
    }

    fun openYoutubeLink(youtubeID: String) {
        val intentApp = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + youtubeID))
        val intentBrowser = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + youtubeID))
        try {
            this.startActivity(intentApp)
        } catch (ex: ActivityNotFoundException) {
            this.startActivity(intentBrowser)
        }

    }
    fun shareUrl(videoId: String) {
        val url = getYoutubeUrl(videoId)
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, url)
        }
        this.startActivity(
            Intent.createChooser(shareIntent, "Share via")
        )
    }
    fun getYoutubeUrl(videoId: String): String {
        return "https://www.youtube.com/watch?v=$videoId"
    }

}