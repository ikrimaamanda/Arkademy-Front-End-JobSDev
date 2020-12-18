package com.example.jobsdev.chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.jobsdev.R
import com.example.jobsdev.home_app.HomeEngineerActivity
import com.example.jobsdev.profile.ProfileAccountEngineerActivity
import com.example.jobsdev.search.SearchActivity
import kotlinx.android.synthetic.main.activity_search.*

class ChatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val homeApp = findViewById<ImageView>(R.id.iv_home_icon)
        homeApp.setOnClickListener {
            val intentHome = Intent(this, HomeEngineerActivity::class.java)
            startActivity(intentHome)
        }

        iv_search_icon.setOnClickListener {
            val intenSearch = Intent(this, SearchActivity::class.java)
            startActivity(intenSearch)
        }

        iv_chat_icon.setOnClickListener {
            val intentChat = Intent(this, ChatActivity::class.java)
            startActivity(intentChat)
        }

        val iconAccountEngineer = findViewById<ImageView>(R.id.iv_account_icon)
        iconAccountEngineer.setOnClickListener {
            val intentAccountEngineer = Intent(this, ProfileAccountEngineerActivity::class.java)
            startActivity(intentAccountEngineer)
        }

    }
}