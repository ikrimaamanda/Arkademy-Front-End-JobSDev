package com.example.jobsdev.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.jobsdev.R
import com.example.jobsdev.databinding.ActivityRegisterBinding
import com.example.jobsdev.sharedpreference.ConstantEngineer
import com.example.jobsdev.sharedpreference.PreferencesHelper

class OnBoardRegisterActivity : AppCompatActivity() {

    private lateinit var sharedPref : PreferencesHelper

    private lateinit var binding : ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_register)

        sharedPref = PreferencesHelper(this)

        binding.btnRegisterEngineer.setOnClickListener {
            sharedPref.putValue(ConstantEngineer.acLevel.toString(), 0)
            startActivity(Intent(this, RegisterEngineerActivity::class.java))
        }

        binding.btnRegisterCompany.setOnClickListener {
            sharedPref.putValue(ConstantEngineer.acLevel.toString(), 1)
            startActivity(Intent(this, RegisterCompanyActivity::class.java))
        }
    }
}