package com.example.jobsdev.maincontent.webview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.jobsdev.R
import com.example.jobsdev.databinding.ActivityGitHubWebViewBinding

class GitHubWebViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGitHubWebViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_git_hub_web_view)
    }
}