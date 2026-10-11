package com.appspartan.avxdownloader

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.appspartan.avxdownloader.databinding.ActivityBrowserBinding

class BrowserActivity : AppCompatActivity() {

    private lateinit var binding:ActivityBrowserBinding
    var url = ""
    var browserFragment : BrowserFragment? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBrowserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        url = intent.extras?.getString(URL_EXTRA).toString()

        openLink(url)

    }

    fun openLink(url: String) {
        val data = Bundle()
        data.putString(URL_EXTRA, this.url)
        browserFragment = BrowserFragment()
        browserFragment?.arguments = data
//        browserFragment?.setPageRightButtonState = setPageRightButtonState
//        browserFragment?.activityFragmentListener = this@BrowserActivity
        try {
            supportFragmentManager.beginTransaction()
                .add(R.id.container, browserFragment!!).commit()
        }catch (e:Exception){}
    }
}