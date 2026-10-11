package com.appspartan.avxdownloader

import android.annotation.TargetApi
import android.database.sqlite.SQLiteCantOpenDatabaseException
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.ConsoleMessage
import android.webkit.DownloadListener
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import com.appspartan.avxdownloader.databinding.FragmentBrowserBinding



class BrowserFragment : Fragment() {

    var url: String? = null
    private var loadedFirsTime: Boolean = false
    private var facebookDownloader = false
    private var twitterDownloader = false

    private var instagramDownloader = false



    private val binding by lazy { FragmentBrowserBinding.inflate(layoutInflater) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        url = arguments?.getString(URL_EXTRA)

        if (!TextUtils.isEmpty(url) && url?.contains("facebook.com") == true)
            facebookDownloader = true
        else if (!TextUtils.isEmpty(url) && url!!.contains("twitter.com"))
            twitterDownloader = true
        else if (!TextUtils.isEmpty(url) && url!!.contains("instagram.com"))
            instagramDownloader = true

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(!loadedFirsTime)
        {
            val thread = HandlerThread("Video Extraction Thread")
            thread.start()
//            extractVideoHandler = Handler(thread.looper)
//
//            createVideosFoundHUD()
//            createFoundVideosWindow()



            val webSettings: WebSettings = binding.webView.settings
            webSettings.javaScriptEnabled = true
            webSettings.domStorageEnabled = true
            webSettings.allowUniversalAccessFromFileURLs = true
            webSettings.javaScriptCanOpenWindowsAutomatically = true


            webSettings.pluginState = WebSettings.PluginState.ON

            webSettings.setSupportZoom(true)
            webSettings.builtInZoomControls = true
            webSettings.displayZoomControls = false
            webSettings.useWideViewPort = true
            webSettings.loadWithOverviewMode = true

            //  webSettings.setMediaPlaybackRequiresUserGesture(false);
            if (facebookDownloader) {
                binding.webView.addJavascriptInterface(this, "FBDownloader")
            }
            if (instagramDownloader) {
                binding.webView.addJavascriptInterface(this, "InstaDownloader")
            }
            if (twitterDownloader) {
                binding.webView.addJavascriptInterface(this, "TwitterDownloader")
            }


            loadWebViewChromeClient()



            if(!TextUtils.isEmpty(url))
                binding.webView.loadUrl(url!!)



                binding.webView.webChromeClient = object  : WebChromeClient(){

                    override fun onProgressChanged(view: WebView?, newProgress: Int) {
//                        activityFragmentListener?.setLoadingProgressValue(newProgress)
                    }

                    override fun onConsoleMessage(consoleMessage: ConsoleMessage): Boolean {

                        return true
                    }

                    override fun onReceivedTitle(view: WebView?, title: String?) {
                        super.onReceivedTitle(view, title)
//                        val vp = VisitedPage()
//                        vp.title = title
//                        vp.link = view!!.url

                        try {
//                            val mHistorySQLite = HistorySQLite(context)
//                            mHistorySQLite.addPageToHistory(vp)
                        } catch (sQLiteCantOpenDatabaseException: SQLiteCantOpenDatabaseException) {
                        } catch (e: java.lang.Exception) {
                        }
                    }

                }

            loadedFirsTime = true

        }
    }


    private fun loadWebViewChromeClient() {
        binding.webView.webChromeClient = object : WebChromeClient() {
            override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                super.onConsoleMessage(consoleMessage)
                /*if (consoleMessage != null) {
                    Log.d("hfih",consoleMessage.message().toString())
                }*/
                return true
            }
        }
        binding.webView.setDownloadListener(DownloadListener { s, s1, s2, s3, l ->
            /* processFacebookVideo(
                 s,
                 System.currentTimeMillis().toString()
             )*/

            try {
                if (s.isNullOrEmpty().not()) {
//                    if (RootHelper.isVideoFile(s)) {
//                        processFacebookVideo(s, System.currentTimeMillis().toString());
//                    } else {
//                        processFacebookImage(s);
//                    }
                }
            }catch (e:Exception){

            }
        })

        binding.webView.webViewClient = object : WebViewClient() {
            //it seems not setting webclient, launches
            //default browser instead of opening the page in webview
            private var currentPage: String? = binding.webView.url
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                return if (!url.startsWith("intent")) {
                    super.shouldOverrideUrlLoading(view, url)
                } else true
            }


            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            override fun shouldOverrideUrlLoading(
                view: WebView,
                request: WebResourceRequest
            ): Boolean {
                return if (!request.url.toString().startsWith("intent")) {
                    super.shouldOverrideUrlLoading(view, request)
                } else true
            }

            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {

//                activityFragmentListener?.showUrlLoadingProgress(true)
                super.onPageStarted(view, url, favicon)
//                if(downloadBtn!=null) {
//                    if (url.startsWith("https://scontent.")) {
//                        downloadBtn?.setVisibility(View.VISIBLE);
//                    } else {
//                        downloadBtn?.setVisibility(View.GONE);
//                    }
//                }

            }


            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)

//                printWeb=view
//
//
//                activityFragmentListener?.showUrlLoadingProgress(false)
//
//                if(downloadBtn!=null) {
//                    if (url.startsWith("https://scontent.")) {
//                        downloadBtn?.setVisibility(View.VISIBLE);
//                    } else {
//                        downloadBtn?.setVisibility(View.GONE);
//                    }
//                }


                if (instagramDownloader) {

                    //Instagram Video

//                    getInstaVideoScript(binding.webView)
                    binding.webView.loadUrl(
                        "javascript:( window.onload=prepareInstaVideo;"
                                + ")()"
                    )

                    ///Instagram Image
                    loadInstScriptImage(binding.webView)
                    binding.webView.loadUrl(
                        ("javascript:( window.onload=prepareInstaImage;"
                                + ")()")
                    )
                }
                else if(facebookDownloader){

//                    loadFbWatchScriptVideo(binding.webView)

                    binding.webView.loadUrl("javascript:( window.onload=prepareFBVideo;" + ")()")

                    loadFbScriptImage(binding.webView)

                    binding.webView.loadUrl("javascript:( window.onload=prepareFacebookImage;" + ")()")

                }
                else if(twitterDownloader){
//                    twitterImageDownload(binding.webView);

                    binding.webView.loadUrl("javascript:( window.onload=prepareTwitterImage;" + ")()")

                }
            }

            override fun onLoadResource(view: WebView, url: String) {

//                setPageRightButtonState?.invoke(binding.webView.canGoForward())


                if (facebookDownloader) {
//                    loadFbWatchScriptVideo(binding.webView)

                    binding.webView.loadUrl("javascript:( window.onload=prepareFBVideo;" + ")()")

                    loadFbScriptImage(binding.webView)

                    binding.webView.loadUrl("javascript:( window.onload=prepareFacebookImage;" + ")()")

                    // onLoadFacebookScript();
                }
                else if (instagramDownloader) {

                    //Instagram Video
//                    getInstaVideoScript(binding.webView)
                    binding.webView.loadUrl(
                        ("javascript:( window.onload=prepareInstaVideo;"
                                + ")()")
                    )

                    ///Instagram Image
                    loadInstScriptImage(binding.webView)
                    binding.webView.loadUrl(
                        ("javascript:( window.onload=prepareInstaImage;"
                                + ")()")
                    )
                }
                else if(twitterDownloader){
//                    twitterImageDownload(binding.webView);

                    binding.webView.loadUrl("javascript:( window.onload=prepareTwitterImage;" + ")()")

                }
                if (!instagramDownloader) {
                    val page = view.url
                    val title = view.title
                    if (page != null && page != currentPage) {
                        currentPage = page
//                        if (videoDetectionInitiator != null) videoDetectionInitiator?.clear()
                    }
//                    if (!ytube) {
//                        videoExtract.setUrl(url)
//                        videoExtract.setTitle(title?:"")
//                        videoExtract.setPage(page?:"")
//                        extractVideoHandler?.post(videoExtract)
//                    }
                }
            }


            override fun shouldInterceptRequest(view: WebView, url: String): WebResourceResponse? {
                return super.shouldInterceptRequest(view, url)
            }

            override fun shouldInterceptRequest(
                view: WebView,
                request: WebResourceRequest
            ): WebResourceResponse? {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    return shouldInterceptRequest(view, request.url.toString())
                }
                return null
            }

        }

    }


    private fun loadInstScriptImage(web_view: WebView?) {
        web_view?.loadUrl(
            ("""javascript:(function prepareInstaImage() { 
function createThumbnail(imageElement) {
    var src = imageElement.src;
    var node = document.createElement("div");
    var imgNode = document.createElement("img");
      imgNode.setAttribute("src", "data:image/png;base64,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");
    imgNode.setAttribute("class", "custom_img");
    imgNode.setAttribute("width", "50");
    imgNode.setAttribute("height", "50");
    imgNode.setAttribute("alt", "Download"); 
    imgNode.setAttribute("style", "position: absolute;margin-left: 84%;margin-top: -53px;");
    node.appendChild(imgNode);
    imgNode.addEventListener('click', function () {
        console.log(src);
        InstaDownloader.processInstaImage(src);
    });
    var pn = imageElement.parentNode;
    pn.parentElement.parentElement.append(node);
}
function checkForNewImages() {
    var images = document.querySelectorAll('._aagv > img');
    images.forEach(image => {
        if (!image.getAttribute('data-thumbnail')) {
            image.setAttribute('data-thumbnail', 'true');
            createThumbnail(image);
        }
    });
}
var initialImages = document.querySelectorAll('._aagv > img');
initialImages.forEach(image => {
    createThumbnail(image);
});
setInterval(checkForNewImages, 500);
                   })()""")
        )
    }

    private fun loadFbScriptImage(webView: WebView?) {
        webView?.loadUrl(

            """javascript:
                
                (function prepareFacebookImage() { 
          
             
              var el =  document.getElementsByTagName('img');
              
                    for(var i=0;i<el.length; i++)
                    {
if(el[i].naturalHeight>500 && el[i].parentNode.nextElementSibling==null && !el[i].parentNode.hasAttribute("data-video-id"))
{      

            var node = document.createElement("H1");
            
           node.setAttribute("class" ,"custom_img");
    
           node.setAttribute("alt", el[i].src);
              
              var node = document.createElement("img");
                      node.setAttribute("src", "data:image/png;base64,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");
        node.setAttribute("width", "50");
        node.setAttribute("height", "50");
       
        node.setAttribute("style", "position: absolute;margin-left: 75%;margin-top: 30%;z-index: 100;border:none;");                     
  el[i].parentNode.appendChild(node);
  el[i].parentNode.setAttribute('alt',el[i].src);
   el[i].parentNode.parentNode.setAttribute('alt',el[i].src);
 node.parentNode.parentNode.onclick = function(event) { 
 
    FBDownloader.processFacebookImage(event.srcElement.getAttribute('alt'));

 };
 
}
                        

}}

)()
"""
        )
    }

    @JavascriptInterface
    fun processFacebookImage(facebookImageSrc: String?) {

        if (!TextUtils.isEmpty(facebookImageSrc)) {
            if(!facebookImageSrc!!.contains(".mp4")){
                activity?.let {
                    BottomSheetUtility.imageParserBottomSheet(
                        it,
                        facebookImageSrc,
                        "facebook",
                        "https://www.facebook.com"
                    )
                }
            }
        }
    }

    @JavascriptInterface
    fun processInstaImage(facebookImageSrc: String?) {
        Log.d("dikshamyjaan","446 $facebookImageSrc")
        if (!TextUtils.isEmpty(facebookImageSrc)) {
            BottomSheetUtility.imageParserBottomSheet(
                requireActivity(),
                facebookImageSrc,
                "instagram",
                "https://www.instagram.com"
            )
        }
    }



}