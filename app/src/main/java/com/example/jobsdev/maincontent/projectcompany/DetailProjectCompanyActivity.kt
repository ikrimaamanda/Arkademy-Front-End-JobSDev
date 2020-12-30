package com.example.jobsdev.maincontent.projectcompany

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.jobsdev.R
import com.example.jobsdev.databinding.ActivityDetailProjectCompanyBinding

class DetailProjectCompanyActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailProjectCompanyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_project_company)
    }
}