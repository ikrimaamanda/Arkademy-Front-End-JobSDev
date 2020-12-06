package com.example.jobsdev

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_register_company.*

class RegisterCompanyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_company)

        val tvLoginCompany = findViewById<TextView>(R.id.tv_login_here)
        tvLoginCompany.setOnClickListener {
            val intentLogin = Intent(this, LoginCompanyActivity::class.java)
            startActivity(intentLogin)
        }

        val btnRegisterCompany = findViewById<Button>(R.id.btn_register)
        btnRegisterCompany.setOnClickListener {
            val intentRegister = Intent(this, LoginCompanyActivity::class.java)
            startActivity(intentRegister)
        }
    }
}