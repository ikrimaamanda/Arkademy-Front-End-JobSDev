package com.example.jobsdev.maincontent.portfolioengineer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.jobsdev.R
import com.example.jobsdev.databinding.ActivityAddPortfolioBinding

class AddPortfolioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddPortfolioBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_portfolio)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}