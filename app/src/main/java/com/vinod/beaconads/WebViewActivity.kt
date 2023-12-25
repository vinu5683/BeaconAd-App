package com.vinod.beaconads

import android.net.http.SslError
import android.os.Bundle
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.vinod.beaconads.databinding.ActivityWebViewBinding


class WebViewActivity : AppCompatActivity() {

    lateinit var binding: ActivityWebViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        BeaconApplication.getInstance().clearAllNotifications()
        addSettingsToWebView()
        loadUrlInWebView()

    }

    private fun loadUrlInWebView() {
        intent?.getStringExtra("urlToLoad")?.let {
            binding.webViewLoader.loadUrl(it)
        }
    }

    private fun addSettingsToWebView() {
        binding.webViewLoader.apply {
            settings.javaScriptEnabled = true
            settings.setSupportZoom(true)
            settings.builtInZoomControls = true
            settings.domStorageEnabled = true // Add this

            settings.javaScriptCanOpenWindowsAutomatically = true
            webViewClient = object : WebViewClient() {
                override fun onReceivedSslError(
                    view: WebView,
                    handler: SslErrorHandler,
                    error: SslError
                ) {
//                    super.onReceivedSslError(view, handler, error)
                    handler.proceed()
                }
            }
        }
    }
}