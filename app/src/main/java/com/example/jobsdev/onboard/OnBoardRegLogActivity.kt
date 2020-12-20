package com.example.jobsdev.onboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.jobsdev.LoginEngineerActivity
import com.example.jobsdev.R
import com.example.jobsdev.databinding.ActivityOnBoardRegLogBinding
import com.example.jobsdev.register.OnBoardRegisterActivity

class OnBoardRegLogActivity : AppCompatActivity() {

    private lateinit var binding : ActivityOnBoardRegLogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_on_board_reg_log)

        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this, OnBoardRegisterActivity::class.java))
        }

        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this, LoginEngineerActivity::class.java))
        }
    }
}