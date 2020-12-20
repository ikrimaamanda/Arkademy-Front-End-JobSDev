package com.example.jobsdev.home_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.jobsdev.profile.ProfileEngineerActivity
import com.example.jobsdev.R
import kotlinx.android.synthetic.main.activity_search.*

class HomeEngineerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_engineer_second)

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