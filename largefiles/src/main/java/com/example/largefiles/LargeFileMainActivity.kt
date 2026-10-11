package com.example.largefiles

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.largefiles.ThemeUtils.formatSize
import com.example.largefiles.ThemeUtils.getAvailableInternalMemorySizeInLong
import com.example.largefiles.ThemeUtils.getTotalInternalMemorySizeInLong
import com.example.largefiles.databinding.LargefileActivityMainBinding

class LargeFileMainActivity : AppCompatActivity() {

    private lateinit var binding: LargefileActivityMainBinding
    var largeFragment:LargeFilesFragment?=null
    var cleanedFilesFragment:CleanFilesFragment?=null
    var filterFragment:FilterDuplicateFragment?=null
    var viewModel : LargeFileViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = LargefileActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        viewModel = ViewModelProvider(this)[LargeFileViewModel::class.java]

        viewModel?.fetchCount(this)

        viewModel?.count_videos?.observe(this, Observer {
            binding.countLargeVideos.text = "$it videos over 25.0 MB"
        })

        viewModel?.count_images?.observe(this, Observer {
            binding.countLargePhotos.text = "$it photos over 5.0 MB"
        })

        viewModel?.size_photos?.observe(this, Observer {
            binding.largePhotosSize.text = "$it"
        })

        viewModel?.size_videos?.observe(this, Observer {
            binding.largeVideoSize.text = "$it"
        })

        val totalSizeInternal = getTotalInternalMemorySizeInLong()
        val availableSizeInternal = getAvailableInternalMemorySizeInLong()
        val consumedSpace = totalSizeInternal-availableSizeInternal

        binding.consumedSpace.text = formatSize(consumedSpace)


        binding.largeVideos.setOnClickListener {
            val fragmentManager: FragmentManager = supportFragmentManager

            if (largeFragment == null || largeFragment?.isAdded == false) {
                largeFragment = LargeFilesFragment.newInstance(VIDEOS_ID)
                val fragmentTransaction: FragmentTransaction =
                    fragmentManager.beginTransaction()
                fragmentTransaction.add(R.id.home_fragment_holder, largeFragment!!)
                    .addToBackStack("")
                fragmentTransaction.commit()
            }

        }

        binding.largePhoto.setOnClickListener {
            val fragmentManager: FragmentManager = supportFragmentManager

            if (largeFragment == null || largeFragment?.isAdded == false) {
                largeFragment = LargeFilesFragment.newInstance(PHOTOS_ID)
                val fragmentTransaction: FragmentTransaction =
                    fragmentManager.beginTransaction()
                fragmentTransaction.add(R.id.home_fragment_holder, largeFragment!!)
                    .addToBackStack("")
                fragmentTransaction.commit()
            }

        }

        binding.duplicateImage.setOnClickListener {
            val fragmentManager: FragmentManager = supportFragmentManager

            if (filterFragment == null || filterFragment?.isAdded == false) {
                filterFragment = FilterDuplicateFragment.newInstance(FilterImage)
                if(filterFragment?.isAdded == false){
                    try {
                        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
                        fragmentTransaction.add(R.id.home_fragment_holder, filterFragment!!).addToBackStack("")

                        fragmentTransaction.commit()
                    }
                    catch (e : Exception){
                    }
                }
            }
        }

        binding.duplicateVideo.setOnClickListener {
            val fragmentManager: FragmentManager = supportFragmentManager

            if (filterFragment == null || filterFragment?.isAdded == false) {
                filterFragment = FilterDuplicateFragment.newInstance(FilterVideo)
                if(filterFragment?.isAdded == false){
                    try {
                        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
                        fragmentTransaction.add(R.id.home_fragment_holder, filterFragment!!)
                            .addToBackStack("")

                        fragmentTransaction.commit()
                    }
                    catch (e : Exception){
                    }
                }
            }
        }

        binding.duplicateAudio.setOnClickListener {
            val fragmentManager: FragmentManager = supportFragmentManager

            if (filterFragment == null || filterFragment?.isAdded == false) {
                filterFragment = FilterDuplicateFragment.newInstance(FilterAudio)
                if(filterFragment?.isAdded == false){
                    try {
                        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
                        fragmentTransaction.add(R.id.home_fragment_holder, filterFragment!!)
                            .addToBackStack("")

                        fragmentTransaction.commit()
                    }
                    catch (e : Exception){
                    }
                }
            }
        }





    }

    fun openfilesCleaned(sizeCleaned:String,){
        if (cleanedFilesFragment == null || cleanedFilesFragment?.isAdded == false) {
            try{
                cleanedFilesFragment = CleanFilesFragment()
                cleanedFilesFragment?.sizeCleaned = sizeCleaned

                supportFragmentManager.popBackStack()
                val fragmentTransaction: FragmentTransaction =
                    supportFragmentManager.beginTransaction()
                fragmentTransaction.add(R.id.home_fragment_holder, cleanedFilesFragment!!)
                    .addToBackStack("")
                fragmentTransaction.commit()
            }catch (e:Exception){}
        }
    }
}