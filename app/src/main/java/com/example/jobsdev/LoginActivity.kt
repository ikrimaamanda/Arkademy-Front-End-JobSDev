package com.example.jobsdev

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.jobsdev.databinding.ActivityLoginBinding
import com.example.jobsdev.maincontent.MainContentActivity
import com.example.jobsdev.onboard.OnBoardRegLogActivity
import com.example.jobsdev.onboard.OnBoardRegisterActivity
import com.example.jobsdev.reset_password.ResetPasswordSendEmailActivity
import com.example.jobsdev.sharedpreference.Constant
import com.example.jobsdev.sharedpreference.PreferencesHelper

class LoginActivity : AppCompatActivity() {

    private lateinit var sharedPref : PreferencesHelper
    private lateinit var binding : ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        sharedPref = PreferencesHelper(this)

        binding.tvRegisterHere.setOnClickListener {
            val intentRegister = Intent(this, OnBoardRegisterActivity::class.java)
            startActivity(intentRegister)
        }

        binding.tvForgotPassword.setOnClickListener {
            val intentForgotPassword = Intent(this, ResetPasswordSendEmailActivity::class.java)
            startActivity(intentForgotPassword)
        }

        binding.btnLogin.setOnClickListener {
            if(binding.etEmail.text.isEmpty() || binding.etPassword.text.isEmpty()) {
                Toast.makeText(this, "Please filled all field", Toast.LENGTH_SHORT).show()
                binding.etEmail.requestFocus()
            } else {
                saveSession(binding.etEmail.text.toString(), binding.etPassword.text.toString())
                showMessage("Login Success")
                moveActivity()
            }
        }
    }

//    override fun onStart() {
//        super.onStart()
//        moveActivity()
//    }

    private fun moveActivity() {
        if (sharedPref.getValueBoolean(Constant.prefIsLogin)!!) {
            startActivity(Intent(this, MainContentActivity::class.java))
            finish()
        }
    }

    private fun saveSession(email : String, password : String) {
            sharedPref.putValue(Constant.prefEmail, email)
            sharedPref.putValue(Constant.prefPassword, password)
            sharedPref.putValue(Constant.prefIsLogin, true)
    }

//    private fun CheckAccount() {
//        val email = sharedPref.getValueString(ConstantEngineer.email)
//        val password = sharedPref.getValueString(ConstantEngineer.password)
//
//        if()
//    }

    private fun showMessage(message : String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, OnBoardRegLogActivity::class.java))
    }

}