package com.example.jobsdev.onboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.jobsdev.LoginCompanyActivity
import com.example.jobsdev.LoginEngineerActivity
import com.example.jobsdev.R
import com.example.jobsdev.databinding.ActivityOnBoardBinding

class OnBoardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnBoardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,
            R.layout.activity_on_board
        )

        binding.btnLoginAsEngineer.setOnClickListener {
            val intentEngineer = Intent(this, LoginEngineerActivity::class.java)
            startActivity(intentEngineer)
        }

        binding.btnLoginAsCompany.setOnClickListener {
            val intentCompany = Intent(this, LoginCompanyActivity::class.java)
            startActivity(intentCompany)
        }
    }
}