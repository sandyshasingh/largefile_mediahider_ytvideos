package com.example.freelancerworkspace

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cleanfiles.CleanFilesActivity
import com.example.freelancerworkspace.databinding.PermissionActivityLayoutBinding

class PermissionActivity: AppCompatActivity() {

    var deniedOnce : Boolean = false
    val permissions =arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    private  val STORAGE_PERMISSION = 23
    private lateinit var binding: PermissionActivityLayoutBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = PermissionActivityLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.allowTag.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                try {
                    //should be replaced with all file access permission
                    if (!Environment.isExternalStorageManager()){
                        val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                        intent.addCategory("android.intent.category.DEFAULT")
                        intent.data =
                            Uri.parse("package:$packageName")

                        permissionAccessLauncher.launch(intent)
                    }
                }catch (e:Exception){
                    try {
                        val obj = Intent()
                        obj.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                        permissionAccessLauncher.launch(obj)

                    }catch (e:Exception){

                    }
                }
            }else {
                ActivityCompat.requestPermissions(this, permissions, STORAGE_PERMISSION)

            }
        }

    }

    private var permissionAccessLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            val checkPermission =  Environment.isExternalStorageManager()
            if(checkPermission) {
//                setResult(RESULT_OK)
//                finish()
                val mainIntent = Intent(this, CleanFilesActivity::class.java)

                startActivity(mainIntent)
                finish()
            }
            else
            {
                deniedOnce = true
                binding.allowTag.text = "OPEN SETTINGS"
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION) {
            val checkPermission= ContextCompat.checkSelfPermission(
                applicationContext, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.WRITE_EXTERNAL_STORAGE
            )== PackageManager.PERMISSION_GRANTED

            if(checkPermission) {
                val mainIntent = Intent(this, CleanFilesActivity::class.java)
                startActivity(mainIntent)
                finish()
            }
            else
            {
                deniedOnce = true
                binding.allowTag.text = "OPEN SETTINGS"
            }
        }
    }
}