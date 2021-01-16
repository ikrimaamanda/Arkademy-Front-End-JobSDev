package com.example.jobsdev.maincontent.portfolioengineer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.jobsdev.R
import com.example.jobsdev.databinding.ActivityDetailPortfolioBinding
import com.example.jobsdev.sharedpreference.ConstantPortfolio
import com.example.jobsdev.sharedpreference.PreferencesHelper

class DetailPortfolioActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailPortfolioBinding
    private lateinit var sharedPref : PreferencesHelper
    private var imgLink = "http://54.236.22.91:4000/image/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_portfolio)
        sharedPref = PreferencesHelper(this)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        val portfolioId = intent.getIntExtra("portfolioId", 0)
        val appName = intent.getStringExtra("appName")
        val desc = intent.getStringExtra("portfolioDesc")
        val linkPub = intent.getStringExtra("linkPub")
        val linkRepo = intent.getStringExtra("linkRepo")
        val workPlace = intent.getStringExtra("workPlace")

        val image = sharedPref.getValueString(ConstantPortfolio.portfolioImage)

        Glide.with(binding.ivPortfolioImage)
            .load(imgLink + image)
            .placeholder(R.drawable.img_loading)
            .error(R.drawable.ic_img_add_portfolio)
            .into(binding.ivPortfolioImage)

        binding.fabUpdateImage.setOnClickListener {
            val intent = Intent(this, UpdatePortfolioImageActivity::class.java)
            intent.putExtra("updatePortfolioId", portfolioId)
            startActivity(intent)
        }

        binding.tvAppName.setText(appName)
        binding.tvDesc.setText(desc)
        binding.tvLinkPub.setText(linkPub)
        binding.tvLinkRepo.setText(linkRepo)
        binding.tvWorkplace.setText(workPlace)

        binding.btnUpdate.setOnClickListener {
            val intent = Intent(this, UpdatePortfolioActivity::class.java)
            intent.putExtra("updatePortfolioId", portfolioId)
            intent.putExtra("appName", appName)
            intent.putExtra("portfolioDesc", desc)
            intent.putExtra("linkPub", linkPub)
            intent.putExtra("linkRepo", linkRepo)
            intent.putExtra("workPlace", workPlace)
            startActivity(intent)
        }

        binding.btnCancel.setOnClickListener {
            onBackPressed()
        }
    }
}