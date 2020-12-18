package com.example.jobsdev.reset_password

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.jobsdev.OnBoardActivity
import com.example.jobsdev.R

class ResetPasswordInputNewPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password_input_new_password)

        val btnInputNewPassword = findViewById<Button>(R.id.btn_reset_password)

        btnInputNewPassword.setOnClickListener {
            val intentResetPassword = Intent(this, OnBoardActivity::class.java)
            startActivity(intentResetPassword)
        }
    }
}