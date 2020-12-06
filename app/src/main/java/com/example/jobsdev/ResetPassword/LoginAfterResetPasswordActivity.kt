package com.example.jobsdev.ResetPassword

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.jobsdev.Profile.ProfileAccountEngineerActivity
import com.example.jobsdev.R

class LoginAfterResetPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_after_reset_password)

        val btnLoginAfterResetPassword = findViewById<Button>(R.id.btn_login)
        btnLoginAfterResetPassword.setOnClickListener {
            val intentLogin = Intent(this, ProfileAccountEngineerActivity::class.java)
            startActivity(intentLogin)
        }
    }
}