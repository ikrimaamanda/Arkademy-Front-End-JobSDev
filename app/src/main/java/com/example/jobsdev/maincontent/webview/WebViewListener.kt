package com.example.jobsdev.maincontent.webview

interface WebViewListener {
    fun onPageStarted()
    fun onPageFinish()
    fun onShouldOverridUrl(redirectUrl : String)
    fun onProgressChange(progress: Int)
}