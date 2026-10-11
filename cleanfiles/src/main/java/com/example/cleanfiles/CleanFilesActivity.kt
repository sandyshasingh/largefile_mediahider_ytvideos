package com.example.cleanfiles

import android.content.Intent
import android.os.Bundle

import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.cleanfiles.databinding.ActivityMainCleanMasterBinding
import com.example.largefiles.LargeFileMainActivity


class CleanFilesActivity : AppCompatActivity(), View.OnClickListener {

    var cleanFragment:CleanFragment?=null
    var cleanFragmentTwo:CleanFragmentTwo?=null

    var listCleanedItems:ArrayList<ApkItem> = ArrayList()


    private lateinit var binding: ActivityMainCleanMasterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainCleanMasterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val totalSizeInternal = BaseConfig(this).getTotalInternalMemorySizeInLong()
        val availableSizeInternal = BaseConfig(this).getAvailableInternalMemorySizeInLong()

        var availablePercent : Float = 0.00f
        var usedSpacePercent : Float = 0.00f
        var arcPercent : Long = 0
        if(totalSizeInternal > 0){
            availablePercent = ((availableSizeInternal.toFloat() * 100) / totalSizeInternal)
            usedSpacePercent = 100 - availablePercent

//            val usedSpacePercent2digits = String.format("%.2f", usedSpacePercent)
//            val availablePercent2digits = String.format("%.2f", availablePercent)
//
//            binding.usedSpace.text = "$usedSpacePercent2digits%"
//            binding.freeSpace.text = "$availablePercent2digits%"


            arcPercent = (((totalSizeInternal - availableSizeInternal) * 100) / totalSizeInternal)


            binding.arcProgress2.max = 100
            binding.arcProgress2.progress = arcPercent.toInt()
            binding.arcProgress2.suffixText = "%"

        }

        binding.backkIcon.setOnClickListener {
            onBackPressed()
        }

        binding.filterDuplicate.setOnClickListener {
            startActivity(Intent(this, LargeFileMainActivity::class.java))
        }

//        binding.btnClean.setOnClickListener(this)
        binding.cleanUp.setOnClickListener(this)


    }


    fun loadFinalFragment(sizeCleaned:String, listItemsCleaned:ArrayList<ApkItem>){

        try {
            listCleanedItems = listItemsCleaned

            runOnUiThread {
                        val fragmentManager: FragmentManager = supportFragmentManager
                        if (cleanFragmentTwo == null || cleanFragmentTwo?.isAdded == false) {
                            try {
                                cleanFragmentTwo = CleanFragmentTwo()
                                cleanFragmentTwo?.sizeCleaned = sizeCleaned
                                cleanFragmentTwo?.deletedList = listItemsCleaned

                                supportFragmentManager.popBackStack()
                                val fragmentTransaction: FragmentTransaction =
                                    supportFragmentManager.beginTransaction()
                                fragmentTransaction.add(R.id.clean_fragment_holder, cleanFragmentTwo!!)
                                    .addToBackStack("")
                                fragmentTransaction.commit()
                            }
                            catch (e : Exception){

                            }

                        }


            }
        }catch (e:IllegalStateException){

        }

    }

    override fun onBackPressed() {
        val tempFragment =
            supportFragmentManager.findFragmentById(R.id.clean_fragment_holder)
        if(tempFragment is CleanFragmentTwo){
            if(cleanFragmentTwo?.isDeletedListShown == true){
                cleanFragmentTwo?.isDeletedListShown = false

            }
            else
                super.onBackPressed()
        }else{
            super.onBackPressed()


        }

    }

    override fun onClick(v: View?) {

                val fragmentManager: FragmentManager = supportFragmentManager

                if (cleanFragment == null || cleanFragment?.isAdded == false) {
                    cleanFragment = CleanFragment()
                    val fragmentTransaction: FragmentTransaction =
                        fragmentManager.beginTransaction()
                    fragmentTransaction.add(R.id.clean_fragment_holder, cleanFragment!!)
                        .addToBackStack("")
                    fragmentTransaction.commit()
                }



    }

}