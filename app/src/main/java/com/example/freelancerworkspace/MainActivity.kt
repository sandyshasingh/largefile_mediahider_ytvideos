package com.example.freelancerworkspace

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cleanfiles.CleanFilesActivity
import com.example.cleanfiles.PERMISSION_WRITE_STORAGE
import com.example.cleanfiles.hasPermission
import com.example.freelancerworkspace.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var isAskingPermissions = false
    var actionOnPermission: ((granted: Boolean) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        handlePermission(PERMISSION_WRITE_STORAGE) {
            if (it) {
                Handler(Looper.getMainLooper()).postDelayed({
                    val mainIntent = Intent(this, CleanFilesActivity::class.java)

                    this.startActivity(mainIntent)
                    this.finish()
                },2000L)
//                initView(savedInstanceState)
            } else {
                Handler(Looper.getMainLooper()).postDelayed({
                    val intent = Intent(this, PermissionActivity::class.java).apply {  }
                    startActivity(intent)
                    this.finish()
                },2000L)
            }
        }

    }

    fun handlePermission(permissionId: Int, callback: (granted: Boolean) -> Unit) {
        actionOnPermission = null

        if (checkPermission(permissionId)) {
            callback(true)
        } else {
            isAskingPermissions = true
            callback(false)

        }
    }

    fun checkPermission(permissionId: Int) : Boolean{
        return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        }
        else{
            hasPermission(permissionId)
        }
    }


    override fun onStop() {
        super.onStop()
        actionOnPermission = null
    }

}