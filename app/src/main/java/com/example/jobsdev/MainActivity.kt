package com.example.jobsdev

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.jobsdev.onboard.OnBoardActivity
import com.example.jobsdev.onboard.OnBoardRegLogActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val handler = Handler(mainLooper)
        handler.postDelayed({
            val intent = Intent(this, OnBoardRegLogActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)

    }
}