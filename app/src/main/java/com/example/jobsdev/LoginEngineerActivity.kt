package com.example.jobsdev

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.jobsdev.databinding.ActivityLoginEngineerBinding
import com.example.jobsdev.maincontent.MainContentActivity
import com.example.jobsdev.register.OnBoardRegisterActivity
import com.example.jobsdev.register.RegisterEngineerActivity
import com.example.jobsdev.reset_password.ResetPasswordSendEmailActivity

class LoginEngineerActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginEngineerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login_engineer)

        binding.tvRegisterHere.setOnClickListener {
            val intentRegister = Intent(this, OnBoardRegisterActivity::class.java)
            startActivity(intentRegister)
        }

        binding.tvForgotPassword.setOnClickListener {
            val intentForgotPassword = Intent(this, ResetPasswordSendEmailActivity::class.java)
            startActivity(intentForgotPassword)
        }

        binding.btnLogin.setOnClickListener {
            val intentLogin = Intent(this, MainContentActivity::class.java)
            startActivity(intentLogin)
            finish()
        }
    }
}