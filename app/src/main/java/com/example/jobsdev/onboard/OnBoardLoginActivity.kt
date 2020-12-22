package com.example.jobsdev.onboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.jobsdev.LoginActivity
import com.example.jobsdev.R
import com.example.jobsdev.databinding.ActivityOnBoardLoginBinding
import com.example.jobsdev.sharedpreference.Constant
import com.example.jobsdev.sharedpreference.PreferencesHelper

class OnBoardLoginActivity : AppCompatActivity() {

    private lateinit var sharedPref : PreferencesHelper
    private lateinit var binding: ActivityOnBoardLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_on_board_login)

        sharedPref = PreferencesHelper(this)

        binding.btnLoginAsEngineer.setOnClickListener {
            sharedPref.putValue(Constant.acLevel.toString(), 0)
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        binding.btnLoginAsCompany.setOnClickListener {
            sharedPref.putValue(Constant.acLevel.toString(), 1)
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}