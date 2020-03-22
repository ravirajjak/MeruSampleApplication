package com.blacksmith.merusampleapplication.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import com.blacksmith.merusampleapplication.R
import com.blacksmith.merusampleapplication.databinding.ActivityWebViewBinding
import com.blacksmith.merusampleapplication.utils.AppConst


class WebViewActivity : BaseActivity() {

    private var mUrl: String? = null
    private lateinit var binding: ActivityWebViewBinding

    companion object {
        fun newInstance(): Class<WebViewActivity> {
            return WebViewActivity::class.java
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_web_view)
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        getIntentData()
        initViews()

    }

    private fun getIntentData() {
        intent.let {
            mUrl = it.getStringExtra(AppConst.PUT_EXTRA_URL)
        }
    }

    private fun initViews() {

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setWebView()
        setToolBarListener()
    }

    private fun setToolBarListener() {

        binding.toolbar.setNavigationOnClickListener { finish() }
    }

    private fun setWebView() {
        val webSettings: WebSettings = binding.actWebWebview.getSettings()
        webSettings.javaScriptEnabled = true

//        val webViewClient = WebViewClientImpl(this)
        binding.actWebWebview.setWebViewClient(WebViewClient())

        binding.actWebWebview.loadUrl(mUrl)
    }

    inner class WebViewClientImpl(val activity: Activity) : WebViewClient() {


        override fun shouldOverrideUrlLoading(webView: WebView?, url: String): Boolean {
            if (mUrl?.let { url.indexOf(it) }!! > -1) return false
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            activity!!.startActivity(intent)
            return true
        }
    }
}
