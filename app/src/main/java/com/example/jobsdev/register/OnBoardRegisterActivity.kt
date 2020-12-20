package com.example.jobsdev.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.jobsdev.R
import com.example.jobsdev.databinding.ActivityRegisterBinding

class OnBoardRegisterActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_register)

        binding.btnRegisterEngineer.setOnClickListener {
            startActivity(Intent(this, RegisterEngineerActivity::class.java))
        }

        binding.btnRegisterCompany.setOnClickListener {
            startActivity(Intent(this, RegisterCompanyActivity::class.java))
        }
    }
}