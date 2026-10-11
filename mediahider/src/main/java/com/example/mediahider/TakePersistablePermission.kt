package com.example.mediahider

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.example.mediahider.utils.StorageHelper
import com.example.mediahider.utils.StorageHelper.OPEN_DIRECTORY_REQUEST_CODE
import com.example.mediahider.utils.StorageHelper.chekPersistPermissionFolderHider
import com.example.mediahider.utils.StorageHelper.getPermissionHider
import com.example.mediahider.databinding.PersistPermissionLayoutBinding

class TakePersistablePermission: AppCompatActivity() {


    private lateinit var binding: PersistPermissionLayoutBinding


    private var finished=false

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = PersistPermissionLayoutBinding.inflate(layoutInflater)

        setContentView(binding.root)


        val from=intent?.getStringExtra("from")

        finished = intent?.getBooleanExtra("finished",false)?:false


        binding.mGoToPermission.setOnClickListener {

//            if (fromHider){

                StorageHelper.openDirectoryHider(this)

//            }else {
//
//                StorageHelper.openDirectoryWhatsapp(this)
//            }

        }
//        if (fromHider){

//            binding.mDescription.text= getString(R.string.to_access_hider_auxiliary_steps_have_to_be_followed_from_your_end)

//        }else{
//            binding.mDescription.text= getString(R.string.to_access_status_folder_permission_is_required)
//        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == OPEN_DIRECTORY_REQUEST_CODE && resultCode == RESULT_OK) {
//            if(fromHider){
                if (data != null && data.data != null && chekPersistPermissionFolderHider(data.data) == true) {
                    getPermissionHider(this, data.data)

                    startApplication()
                } else {
                    showTryAgainDialog()
                }
//            }else {
//                if (data != null && data.data != null && chekPersistPermissionFolder(data.data)) {
//                    getPermission(this, data.data)
//                    startApplication()
//                } else {
//                    showTryAgainDialog()
//                }
//            }
        } else {
            showTryAgainDialog()
        }
    }

    private fun startApplication() {

        try {
            val intent = Intent(
                this,
                SetPinActivity::class.java
            )
            startActivity(intent)

            finish()

        }catch (_:Exception){

        }

    }

    private fun showTryAgainDialog() {

        val dialog = Dialog(this)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

//        val binding: TryAgainLayoutBinding by lazy { TryAgainLayoutBinding.inflate(dialog.layoutInflater) }

        dialog.setContentView(binding.root)

        dialog.show()

//        if (fromHider){

//            binding.mGuideText.text = getString(R.string.selected_folder_is_incorrect_please_try_again_and_click_on_use_this_folder_button)

//        }else {
//
//            binding.mGuideText.text =
//                getString(R.string.selected_folder_is_incorrect_or_update_whatsapp_from_google_play_store)
//
//        }

//        binding.mTryAgainPermission.setOnClickListener {
//
//            dialog.show()
//
////            if (fromHider){
//
//                StorageHelper.openDirectoryHider(this)
//
////            }else {
////
////                StorageHelper.openDirectoryWhatsapp(this)
////            }
//        }
    }
}