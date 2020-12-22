package com.example.jobsdev.maincontent.editprofile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.jobsdev.R
import com.example.jobsdev.databinding.ActivityEditAccountCompanyBinding

class EditAccountCompanyActivity : AppCompatActivity() {

    private lateinit var binding : ActivityEditAccountCompanyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_account_company)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_account_company)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}