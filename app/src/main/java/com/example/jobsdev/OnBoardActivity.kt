package com.example.jobsdev

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class OnBoardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_board)

        val btnLoginEngineer = findViewById<Button>(R.id.btn_login_as_engineer)
        val btnLoginCompany = findViewById<Button>(R.id.btn_login_as_company)

        btnLoginEngineer.setOnClickListener {
            val intentEngineer = Intent(this, LoginEngineerActivity::class.java)
            startActivity(intentEngineer)
        }

        btnLoginCompany.setOnClickListener {
            val intentCompany = Intent(this, LoginCompanyActivity::class.java)
            startActivity(intentCompany)
        }
    }
}