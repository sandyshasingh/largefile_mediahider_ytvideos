package com.example.mediahider

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mediahider.utils.AppThemePrefrences.GetSharedPreference
import com.example.mediahider.utils.PASSWORD_KEY

class HiderSplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_hider_splash_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            decideNavigation()
        }, 1000)
    }

    fun decideNavigation(){
      val  password = GetSharedPreference(this, PASSWORD_KEY)
        if (TextUtils.isEmpty(password)) {
            val intent = Intent(
                this,
                HiderFeatureIntroScreen::class.java
            )
            startActivity(intent)

            finish()
        }else{
            val intent = Intent(
                this,
                EnterPin::class.java
            )
            startActivity(intent)

            finish()
        }

    }
}