package com.example.jobsdev.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.example.jobsdev.R
import com.example.jobsdev.databinding.ActivityProfileCompanyBinding
import kotlinx.android.synthetic.main.activity_search.*

class ProfileCompanyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileCompanyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_profile_company)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile_company)

        val tvFullName = findViewById<TextView>(R.id.tv_full_name)

        val fullName = intent.getStringExtra("fullName")
        tvFullName.text = fullName

        val profileCompany = findViewById<ImageView>(R.id.iv_account_icon)
        profileCompany.setOnClickListener {
            val intentProfile = Intent(this, ProfileCompanyActivity::class.java)
            startActivity(intentProfile)
        }
    }
}