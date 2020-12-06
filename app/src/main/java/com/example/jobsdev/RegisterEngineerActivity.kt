package com.example.jobsdev

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class RegisterEngineerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_engineer)

        val btnRegisterEngineer = findViewById<Button>(R.id.btn_register)
        btnRegisterEngineer.setOnClickListener {
            val intentLogin = Intent(this, LoginEngineerActivity::class.java)
            startActivity(intentLogin)
        }

        val tvLoginEngineer = findViewById<TextView>(R.id.tv_login_here)
        tvLoginEngineer.setOnClickListener {
            val intentLogin = Intent(this, LoginEngineerActivity::class.java)
            startActivity(intentLogin)
        }
    }
}