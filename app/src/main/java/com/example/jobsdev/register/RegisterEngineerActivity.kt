package com.example.jobsdev.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.jobsdev.login.LoginActivity
import com.example.jobsdev.R
import com.example.jobsdev.databinding.ActivityRegisterEngineerBinding
import com.example.jobsdev.sharedpreference.Constant
import com.example.jobsdev.sharedpreference.ConstantEngineer
import com.example.jobsdev.sharedpreference.PreferencesHelper

class RegisterEngineerActivity : AppCompatActivity() {

    private lateinit var sharedPref : PreferencesHelper
    private lateinit var binding : ActivityRegisterEngineerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_register_engineer)

        sharedPref = PreferencesHelper(this)

        binding.btnRegister.setOnClickListener {
            if(binding.etName.text.isEmpty() || binding.etEmail.text.isEmpty() || binding.etNumberPhone.text.isEmpty() || binding.etPassword.text.isEmpty() || binding.etConfirmPassword.text.isEmpty()) {
                Toast.makeText(this, "Please filled all field", Toast.LENGTH_SHORT).show()
                binding.etName.requestFocus()
            } else if(binding.etConfirmPassword.text.toString() != binding.etPassword.text.toString()) {
                Toast.makeText(this, "Please write again password", Toast.LENGTH_SHORT).show()
                binding.etConfirmPassword.requestFocus()
            }
            else {
                saveSession(binding.etName.text.toString(), binding.etEmail.text.toString(), binding.etNumberPhone.text.toString(), binding.etPassword.text.toString(), binding.etConfirmPassword.text.toString())
                showMessage("Registration Success")
                moveActivity()
            }
        }

        binding.tvLoginHere.setOnClickListener {
            val intentLogin = Intent(this, LoginActivity::class.java)
            startActivity(intentLogin)
        }
    }

    private fun saveSession(name: String, email : String, phoneNumber : String, password : String, confirmPassword: String) {
        sharedPref.putValue(ConstantEngineer.fullName, name)
        sharedPref.putValue(Constant.prefPhoneNumber, phoneNumber)
        sharedPref.putValue(Constant.prefEmail, email)
        sharedPref.putValue(Constant.prefPassword, password)
        sharedPref.putValue(Constant.prefConfirmPassword, confirmPassword)
        sharedPref.putValue(Constant.prefIsRegis, true)
    }

    private fun showMessage(message : String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    private fun moveActivity() {
        if (sharedPref.getValueBoolean(Constant.prefIsRegis)!!) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}