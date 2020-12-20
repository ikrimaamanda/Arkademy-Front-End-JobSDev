package com.example.jobsdev.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.example.jobsdev.home_app.HomeCompanyActivity
import com.example.jobsdev.R
import com.example.jobsdev.chat.ChatActivity
import com.example.jobsdev.databinding.ActivityProfileCompanyBinding
import com.example.jobsdev.search.SearchActivity
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

        val homeApp = findViewById<ImageView>(R.id.iv_home_icon)
        homeApp.setOnClickListener {
            val intentHomeApp = Intent(this, HomeCompanyActivity::class.java)
            startActivity(intentHomeApp)
        }

        iv_search_icon.setOnClickListener {
            val intentSearch = Intent(this, SearchActivity::class.java)
            startActivity(intentSearch)
        }

        iv_chat_icon.setOnClickListener {
            val intentChat = Intent(this, ChatActivity::class.java)
            startActivity(intentChat)
        }

        val profileCompany = findViewById<ImageView>(R.id.iv_account_icon)
        profileCompany.setOnClickListener {
            val intentProfile = Intent(this, ProfileCompanyActivity::class.java)
            startActivity(intentProfile)
        }
    }
}