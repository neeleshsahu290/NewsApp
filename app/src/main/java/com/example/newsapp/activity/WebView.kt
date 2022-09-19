package com.example.newsapp.activity

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.newsapp.databinding.ActivityWebViewBinding
import com.example.newsapp.utility.Constants
import com.example.newsapp.utility.toastMsg

class WebView : AppCompatActivity() {
    var url: String? = null
    lateinit var binding: ActivityWebViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        setWebView()
    }

    private fun init() {
        url = intent?.getStringExtra(Constants.URL)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setWebView() {
        binding.webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(
                view: android.webkit.WebView?,
                url: String?,
                favicon: Bitmap?
            ) {
                super.onPageStarted(view, url, favicon)
                binding.progressBar.visibility = View.VISIBLE
            }
            override fun onPageFinished(view: android.webkit.WebView?, url: String?) {
                super.onPageFinished(view, url)
                binding.progressBar.visibility = View.GONE
            }
        }
        Log.d("webview_url", url.toString())
        if (url != null) {
            binding.webView.loadUrl(url!!)
            binding.webView.settings.javaScriptEnabled = true
        } else {
            toastMsg("Some Error Occurred")
        }
    }
}