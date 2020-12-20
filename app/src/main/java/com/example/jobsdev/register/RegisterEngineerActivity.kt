package com.example.jobsdev.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.example.jobsdev.LoginEngineerActivity
import com.example.jobsdev.R
import com.example.jobsdev.databinding.ActivityRegisterEngineerBinding

class RegisterEngineerActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRegisterEngineerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_register_engineer)

        binding.btnRegister.setOnClickListener {
            val intentLogin = Intent(this, LoginEngineerActivity::class.java)
            startActivity(intentLogin)
        }

        binding.tvLoginHere.setOnClickListener {
            val intentLogin = Intent(this, LoginEngineerActivity::class.java)
            startActivity(intentLogin)
        }
    }
}