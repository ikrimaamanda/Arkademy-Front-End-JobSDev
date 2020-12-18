package com.example.jobsdev.home_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.jobsdev.profile.ProfileCompanyActivity
import com.example.jobsdev.profile.ProfileEngineerActivity
import com.example.jobsdev.R
import com.example.jobsdev.chat.ChatActivity
import com.example.jobsdev.search.SearchActivity
import kotlinx.android.synthetic.main.activity_home_company.*
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.activity_search.iv_chat_icon
import kotlinx.android.synthetic.main.activity_search.iv_search_icon

class HomeCompanyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_company)

        val profileEngineer = findViewById<ConstraintLayout>(R.id.engineer_1)
        profileEngineer.setOnClickListener {
            val intentProfileEngineer = Intent(this, ProfileEngineerActivity::class.java)
            startActivity(intentProfileEngineer)
        }

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

        val iconProfileCompany = findViewById<ImageView>(R.id.iv_account_icon)
        iconProfileCompany.setOnClickListener {
            val intentProfileCompany = Intent(this, ProfileCompanyActivity::class.java)
            startActivity(intentProfileCompany)
        }
    }
}