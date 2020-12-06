package com.example.jobsdev

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class RegisterEngineerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_engineer)

        val tvRegisterEngineer = findViewById<Button>(R.id.btn_login)

        tvRegisterEngineer.setOnClickListener {
            val intentLogin = Intent(this, LoginEngineerActivity::class.java)
            startActivity(intentLogin)
        }
    }
}