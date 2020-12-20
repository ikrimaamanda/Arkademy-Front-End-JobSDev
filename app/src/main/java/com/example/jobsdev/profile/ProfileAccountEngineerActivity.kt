package com.example.jobsdev.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.example.jobsdev.home_app.HomeEngineerActivity
import com.example.jobsdev.R
import com.example.jobsdev.adapter.TabPagerAdapter
import com.example.jobsdev.chat.ChatActivity
import com.example.jobsdev.databinding.ActivityProfileAccountEngineerBinding
import com.example.jobsdev.search.SearchActivity
import kotlinx.android.synthetic.main.activity_profile_account_engineer.*
import kotlinx.android.synthetic.main.activity_search.iv_chat_icon
import kotlinx.android.synthetic.main.activity_search.iv_search_icon

class ProfileAccountEngineerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileAccountEngineerBinding
    private lateinit var pagerAdapter : TabPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile_account_engineer)
        pagerAdapter = TabPagerAdapter(supportFragmentManager)

        view_pager.adapter = pagerAdapter
        tab_layout.setupWithViewPager(view_pager)

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