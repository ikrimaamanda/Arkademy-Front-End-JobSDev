package com.example.jobsdev.reset_password

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.jobsdev.R

class ResetPasswordSendEmailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password_send_email)

        val btnResetPassword = findViewById<Button>(R.id.btn_reset_password)

        btnResetPassword.setOnClickListener {
            val intentResetPassword = Intent(this, ResetPasswordInputNewPasswordActivity::class.java)
            startActivity(intentResetPassword)
        }
    }
}