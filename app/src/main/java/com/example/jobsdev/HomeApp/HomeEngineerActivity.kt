package com.example.jobsdev.HomeApp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.jobsdev.Profile.ProfileAccountEngineerActivity
import com.example.jobsdev.Profile.ProfileEngineerActivity
import com.example.jobsdev.R

class HomeEngineerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_engineer_second)

        val iconAccountEngineer = findViewById<ImageView>(R.id.iv_account_icon)
        iconAccountEngineer.setOnClickListener {
            val intentAccounteEngineer = Intent(this, ProfileAccountEngineerActivity::class.java)
            startActivity(intentAccounteEngineer)
        }

        val engineerOne = findViewById<ConstraintLayout>(R.id.engineer_1)
        engineerOne.setOnClickListener {
            val intentProfileEngineer = Intent(this, ProfileEngineerActivity::class.java)
            startActivity(intentProfileEngineer)
        }

        val homeApp = findViewById<ImageView>(R.id.iv_home_icon)
        homeApp.setOnClickListener {
            val intentHome = Intent(this, HomeEngineerActivity::class.java)
            startActivity(intentHome)
        }

    }
}