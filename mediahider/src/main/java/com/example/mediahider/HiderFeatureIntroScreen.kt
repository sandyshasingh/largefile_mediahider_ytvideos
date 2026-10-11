package com.example.mediahider

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mediahider.databinding.ActivityHiderFeatureIntroScreenBinding
import com.example.mediahider.utils.OnBoardAdapter
import com.example.mediahider.utils.onPageChangeListener

class HiderFeatureIntroScreen : AppCompatActivity() {

    var curPos = 0
    private lateinit var binding: ActivityHiderFeatureIntroScreenBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHiderFeatureIntroScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        setContentView(R.layout.activity_hider_feature_intro_screen)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.startBtn.setOnClickListener {
            val intent = Intent(
                this,
                TakePersistablePermission::class.java
            )
            startActivity(intent)
            finish()
        }



        binding.onboardViewPager.onPageChangeListener {

            curPos = it
//            if(it==2){
//                binding.nextitem.text = "Got it"
//            }

            val lastPageIndex = binding.onboardViewPager.adapter?.count?.minus(1) ?: 0

            if (it == lastPageIndex) {
                binding.nextitem.visibility = View.GONE
                binding.btnSkip.visibility = View.GONE
                binding.startBtn.visibility = View.VISIBLE
            } else {
                binding.nextitem.visibility = View.VISIBLE
                binding.btnSkip.visibility = View.VISIBLE
                binding.startBtn.visibility = View.GONE
            }

            /*if (it == 2 && !hideSkip) {

                btn_skip?.text = "Got it"
            }*/
        }
        binding.onboardViewPager.adapter = OnBoardAdapter(supportFragmentManager)
        binding.circleIndicator.setViewPager(binding.onboardViewPager)

        binding.nextitem.setOnClickListener {


//            if (curPos==2){
//                finish()
//            }
//            else{
                curPos++
                binding.onboardViewPager.currentItem = curPos
//            }
        }

        binding.btnSkip.setOnClickListener {
            val intent = Intent(
                this,
                TakePersistablePermission::class.java
            )
            startActivity(intent)
            finish()
        }


    }
}