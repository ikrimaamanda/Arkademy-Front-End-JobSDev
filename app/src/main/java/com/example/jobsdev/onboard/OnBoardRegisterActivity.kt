package com.example.jobsdev.onboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.jobsdev.R
import com.example.jobsdev.databinding.ActivityOnBoardRegisterBinding
import com.example.jobsdev.register.RegisterCompanyActivity
import com.example.jobsdev.register.RegisterEngineerActivity
import com.example.jobsdev.sharedpreference.Constant
import com.example.jobsdev.sharedpreference.PreferencesHelper

class OnBoardRegisterActivity : AppCompatActivity() {

    private lateinit var sharedPref : PreferencesHelper

    private lateinit var binding : ActivityOnBoardRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_on_board_register)

        sharedPref = PreferencesHelper(this)

        binding.btnRegisterEngineer.setOnClickListener {
            sharedPref.putValue(Constant.prefLevel, 0)
            startActivity(Intent(this, RegisterEngineerActivity::class.java))
        }

        binding.btnRegisterCompany.setOnClickListener {
            sharedPref.putValue(Constant.prefLevel, 1)
            startActivity(Intent(this, RegisterCompanyActivity::class.java))
        }
    }
}