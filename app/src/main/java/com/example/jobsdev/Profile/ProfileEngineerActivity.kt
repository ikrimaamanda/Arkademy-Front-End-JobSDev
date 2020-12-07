package com.example.jobsdev.Profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.jobsdev.HomeApp.HomeEngineerActivity
import com.example.jobsdev.R

class ProfileEngineerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_engineer)

        val iconAccountEngineer = findViewById<ImageView>(R.id.iv_account_icon)
        iconAccountEngineer.setOnClickListener {
            val intentAccounteEngineer = Intent(this, ProfileAccountEngineerActivity::class.java)
            startActivity(intentAccounteEngineer)
        }

        val homeApp = findViewById<ImageView>(R.id.iv_home_icon)
        homeApp.setOnClickListener {
            val intentHome = Intent(this, HomeEngineerActivity::class.java)
            startActivity(intentHome)
        }
    }
}