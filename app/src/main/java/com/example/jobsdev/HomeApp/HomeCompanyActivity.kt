package com.example.jobsdev.HomeApp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.jobsdev.Profile.ProfileEngineerActivity
import com.example.jobsdev.R

class HomeCompanyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_company)

        val ivAccountIcon = findViewById<ImageView>(R.id.iv_account_icon)

        ivAccountIcon.setOnClickListener {
            val intentAccount = Intent(this, ProfileEngineerActivity::class.java)
            startActivity(intentAccount)
        }
    }
}