package com.example.jobsdev.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.jobsdev.home_app.HomeEngineerActivity
import com.example.jobsdev.R

class ProfileEngineerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_engineer)

        val homeApp = findViewById<ImageView>(R.id.iv_home_icon)
        homeApp.setOnClickListener {
            val intentHome = Intent(this, HomeEngineerActivity::class.java)
            startActivity(intentHome)
        }
    }
}