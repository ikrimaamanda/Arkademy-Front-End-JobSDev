package com.example.jobsdev.maincontent.hireengineer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.jobsdev.R
import com.example.jobsdev.databinding.ActivityDetailPortfolioEngineerBinding

class DetailPortfolioEngineerActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailPortfolioEngineerBinding
    var imgLink = "http://54.236.22.91:4000/image/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_portfolio_engineer)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.tvAppName.text = intent.getStringExtra("appName")
        binding.tvLinkPub.text = intent.getStringExtra("linkPub")
        binding.tvLinkRepo.text = intent.getStringExtra("linkRepo")
        binding.tvWorkplace.text = intent.getStringExtra("workplace")
        binding.tvTypeApp.text = intent.getStringExtra("typeApp")
        binding.tvDesc.text = intent.getStringExtra("desc")

        val image = intent.getStringExtra("portfolioImage")
        Glide.with(binding.ivPortfoliImage)
            .load(imgLink+image)
            .placeholder(R.drawable.ic_add_project_image)
            .error(R.drawable.ic_add_project_image)
            .into(binding.ivPortfoliImage)
    }
}