package com.example.jobsdev.maincontent.webview

import android.graphics.Bitmap
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import com.example.jobsdev.R
import com.example.jobsdev.databinding.ActivityGitHubWebViewBinding

class GitHubWebViewActivity : AppCompatActivity(), WebViewListener {

    private lateinit var binding: ActivityGitHubWebViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_git_hub_web_view)
        binding.githubWebView.loadUrl("https://github.com/ikrimaamanda")

        binding.githubWebView.webChromeClient = GitHubChromeClient(this)
        binding.githubWebView.webViewClient = GitHubWebClient(this)

    }

    class GitHubChromeClient(private var listener : WebViewListener) : WebChromeClient() {
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            listener.onProgressChange(newProgress)
            super.onProgressChanged(view, newProgress)
        }
    }

    class GitHubWebClient(private var listener : WebViewListener) : WebViewClient() {
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            listener.onPageStarted()
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            listener.onPageFinish()
        }

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            listener.onShouldOverridUrl(request?.url?.toString().orEmpty())
            return super.shouldOverrideUrlLoading(view, request)
        }

    }

    override fun onPageStarted() {
        binding.progressBar.visibility = View.VISIBLE
    }

    override fun onPageFinish() {
        binding.progressBar.visibility = View.GONE
    }

    override fun onShouldOverridUrl(redirectUrl: String) {
        Toast.makeText(this, "redirect url : $redirectUrl", Toast.LENGTH_SHORT).show()
    }

    override fun onProgressChange(progress: Int) {
        binding.progressBar.progress = progress
    }

    override fun onBackPressed() {
        if(binding.githubWebView.canGoBack()) {
            binding.githubWebView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}