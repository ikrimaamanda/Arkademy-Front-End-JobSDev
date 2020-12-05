package com.example.jobsdev

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.jobsdev.HomeApp.HomeCompanyActivity
import com.example.jobsdev.ResetPassword.ResetPasswordSendEmailActivity

class LoginEngineerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_engineer)

        val tvRegisterEngineer = findViewById<TextView>(R.id.tv_register_here)

        tvRegisterEngineer.setOnClickListener {
            val intentRegister = Intent(this, RegisterEngineerActivity::class.java)
            startActivity(intentRegister)
        }

        val tvForgotPassword = findViewById<TextView>(R.id.tv_forgot_password)

        tvForgotPassword.setOnClickListener {
            val intentForgotPassword = Intent(this, ResetPasswordSendEmailActivity::class.java)
            startActivity(intentForgotPassword)
        }

        val btnLogin = findViewById<Button>(R.id.btn_login)

        btnLogin.setOnClickListener {
            val intentLogin = Intent(this, HomeCompanyActivity::class.java)
            startActivity(intentLogin)
        }
    }
}