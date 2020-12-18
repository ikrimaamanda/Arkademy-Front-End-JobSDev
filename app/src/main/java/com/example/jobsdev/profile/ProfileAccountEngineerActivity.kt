package com.example.jobsdev.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.example.jobsdev.home_app.HomeEngineerActivity
import com.example.jobsdev.R
import com.example.jobsdev.chat.ChatActivity
import com.example.jobsdev.search.SearchActivity
import kotlinx.android.synthetic.main.activity_search.*

class ProfileAccountEngineerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_account_engineer)

        val tvFullName = findViewById<TextView>(R.id.tv_full_name)

        val fullName = intent.getStringExtra("fullName")
        tvFullName.text = fullName

        val homeApp = findViewById<ImageView>(R.id.iv_home_icon)
        homeApp.setOnClickListener {
            val intentHome = Intent(this, HomeEngineerActivity::class.java)
            startActivity(intentHome)
        }

        iv_search_icon.setOnClickListener {
            val intentSearch = Intent(this, SearchActivity::class.java)
            startActivity(intentSearch)
        }

        iv_chat_icon.setOnClickListener {
            val intentChat = Intent(this, ChatActivity::class.java)
            startActivity(intentChat)
        }

        val iconAccountEngineer = findViewById<ImageView>(R.id.iv_account_icon)
        iconAccountEngineer.setOnClickListener {
            val intentAccounteEngineer = Intent(this, ProfileAccountEngineerActivity::class.java)
            startActivity(intentAccounteEngineer)
        }

    }
}