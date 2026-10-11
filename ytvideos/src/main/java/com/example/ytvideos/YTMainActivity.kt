package com.example.ytvideos

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.ytvideos.adapters.CategoryAdapter
import com.example.ytvideos.databinding.ActivityYtmainBinding
import com.example.ytvideos.dataclasses.CategoriesList
import com.example.ytvideos.dataclasses.ListItem
import com.example.ytvideos.dataclasses.YoutubeVideoItem
import com.example.ytvideos.listeners.CategoryclickedListener
import com.example.ytvideos.listeners.ItemClickedListener
import com.example.ytvideos.retrofit.ApiRepository
import com.example.ytvideos.retrofit.ApiResult
import com.example.ytvideos.retrofit.AuthViewModel
import com.example.ytvideos.retrofit.AuthViewModelFactory
import com.example.ytvideos.retrofit.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.ArrayList

class YTMainActivity : AppCompatActivity(),CategoryclickedListener,ItemClickedListener {

    private lateinit var binding:ActivityYtmainBinding
//    private lateinit var viewModel: ViewModels

    private lateinit var viewModel: AuthViewModel  // <-- declare here
    var category_adapter: CategoryAdapter? = null
    val youtubeResultsMap = mutableMapOf<String, List<YoutubeVideoItem>>()




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityYtmainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val repository = ApiRepository(RetrofitClient.apiService)
        val factory = AuthViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[AuthViewModel::class.java]

        viewModel.homeUiItems.observe(this) { list ->

//            category_adapter = CategoryAdapter(
//                this@YTMainActivity,
//
//                this@YTMainActivity,
//            )
            category_adapter?.submitList(list)
//            binding.rvCategory.adapter = category_adapter
        }




        CoroutineScope(Dispatchers.IO).launch {
            val categoryResponse: CategoriesList? =
                RemoteConfigUtils.getCategoriesList()

            Log.d("diksha ki ","${categoryResponse?.data}")
            CoroutineScope(Dispatchers.Main).launch {

                categoryResponse?.data?.let {
                    viewModel.loadHomeData(it)
                    category_adapter =  CategoryAdapter(
                        this@YTMainActivity,
                        this@YTMainActivity,
                        this@YTMainActivity
                    )
                }
                    binding.rvCategory.adapter = category_adapter

            }
        }



        viewModel.apiResponse.observe(this) { result ->
            when (result) {
                is ApiResult.Loading -> {
                    // show progress bar
                }
                is ApiResult.Success -> {
//                    val json = result.data.asJsonObject
//                    val status = json["status"].asBoolean
//                    val message = json["message"].asString
                    val videos = result.data.items  // <-- List<YoutubeVideoItem>
//                    adapter.submitList(videos)
                    Toast.makeText(this, "✅ $result", Toast.LENGTH_SHORT).show()
                    Log.d("dikshamerijaan","$videos")
                }
                is ApiResult.Error -> {
                    Toast.makeText(this, "❌ $result", Toast.LENGTH_SHORT).show()
                    Log.d("dikshamerijaan","$result")
                }
            }
        }

        binding.hitbtn.setOnClickListener {
            viewModel.getYoutubeData("yoga")
        }
    }



    override fun categoryItem(categoryList: List<ListItem>) {
        val intent = Intent(this, CategoryData::class.java).apply {
        }
        intent.putParcelableArrayListExtra("HOME_ITEMS", ArrayList(categoryList))
        intent.putExtra("LOAD_DATA",false)
        startActivity(intent)
    }

    override fun playItem(isShare: Boolean, url: String) {

    }



    override fun itmClicked(isPlay: Boolean, title: String, url: String) {
        if(isPlay)
            openYoutubeLink(url)
        else{
            val intent = Intent(this, CategoryData::class.java).apply {
            }
            Log.d("dikshajaanu", "92: ${title}")

            intent.putExtra("ITEM_TITLE", title)
            intent.putExtra("LOAD_DATA",true)
            startActivity(intent)
        }


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
}