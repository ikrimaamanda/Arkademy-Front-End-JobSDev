package com.example.jobsdev

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.jobsdev.maincontent.MainContentActivity
import com.example.jobsdev.register.OnBoardRegisterActivity
import com.example.jobsdev.register.RegisterCompanyActivity
import com.example.jobsdev.reset_password.ResetPasswordSendEmailActivity

class LoginCompanyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_company)

        val tvRegisterCompany = findViewById<TextView>(R.id.tv_register_here)
        tvRegisterCompany.setOnClickListener {
            val intentRegister = Intent(this, OnBoardRegisterActivity::class.java)
            startActivity(intentRegister)
        }

        val tvForgotPassword = findViewById<TextView>(R.id.tv_forgot_password)
        tvForgotPassword.setOnClickListener {
            val intentForgotPassword = Intent(this, ResetPasswordSendEmailActivity::class.java)
            startActivity(intentForgotPassword)
        }

        val btnLogin = findViewById<Button>(R.id.btn_login)
        val etFullName = findViewById<EditText>(R.id.et_email)
        btnLogin.setOnClickListener {
            val fullName = etFullName.text.toString()
            val intentLogin = Intent(this, MainContentActivity::class.java)
            startActivity(intentLogin)
            finish()
        }
    }
}