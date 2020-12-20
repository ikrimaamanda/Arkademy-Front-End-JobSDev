package com.example.jobsdev

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.jobsdev.maincontent.MainContentActivity
import com.example.jobsdev.onboard.OnBoardRegLogActivity
import com.example.jobsdev.sharedpreference.Constant
import com.example.jobsdev.sharedpreference.PreferencesHelper

class MainActivity : AppCompatActivity() {

    private lateinit var sharedPref : PreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPref = PreferencesHelper(this)

        val handler = Handler(mainLooper)
        handler.postDelayed({
            if(sharedPref.getValueBoolean(Constant.prefIsLogin)!!) {
                startActivity(Intent(this, MainContentActivity::class.java))
            } else {
                val intent = Intent(this, OnBoardRegLogActivity::class.java)
                startActivity(intent)
            }
            finish()
        }, 3000)

    }
}