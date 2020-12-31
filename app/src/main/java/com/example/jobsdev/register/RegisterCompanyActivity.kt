package com.example.jobsdev.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.jobsdev.login.LoginActivity
import com.example.jobsdev.R
import com.example.jobsdev.databinding.ActivityRegisterCompanyBinding
import com.example.jobsdev.databinding.ActivityRegisterEngineerBinding
import com.example.jobsdev.remote.ApiClient
import com.example.jobsdev.sharedpreference.Constant
import com.example.jobsdev.sharedpreference.PreferencesHelper
import kotlinx.coroutines.*

class RegisterCompanyActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRegisterCompanyBinding
    private lateinit var sharedPref : PreferencesHelper
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service : RegistrationApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register_company)

        sharedPref = PreferencesHelper(this)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(context = this)!!.create(RegistrationApiService::class.java)

        binding.tvLoginHere.setOnClickListener {
            val intentLogin = Intent(this, LoginActivity::class.java)
            startActivity(intentLogin)
        }

        binding.btnRegister.setOnClickListener {
            if(binding.etName.text.isEmpty() || binding.etEmail.text.isEmpty() || binding.etNumberPhone.text.isEmpty() || binding.etPassword.text.isEmpty() || binding.etConfirmPassword.text.isEmpty()) {
                Toast.makeText(this, "Please filled all field", Toast.LENGTH_SHORT).show()
                binding.etName.requestFocus()
            } else if(binding.etConfirmPassword.text.toString() != binding.etPassword.text.toString()) {
                showMessage("Please write again password")
                binding.etConfirmPassword.requestFocus()
            }
            else {
                callRegistrationApi(binding.etName.text.toString(), binding.etEmail.text.toString(), binding.etNumberPhone.text.toString(), binding.etPassword.text.toString(), binding.etCompany.text.toString(), binding.etPosition.text.toString())
            }
        }

    }

    private fun callRegistrationApi(name : String, email : String, phoneNumber: String, password : String, companyName : String, position : String) {
        coroutineScope.launch {
            val result =  withContext(Dispatchers.IO) {
                try {
                    service.registrationCompanyReq(name, email, phoneNumber, password, 1, companyName, position)
                } catch (e : Throwable) {
                    e.printStackTrace()
                }
            }

            if(result is RegistrationResponse) {
                Log.d("registrationComReq : ", result.toString())

                if(result.success) {
                    showMessage("Registration Success!")
                    moveActivity()
                } else {
                    showMessage(result.message)
                }
            }
        }
    }

    private fun showMessage(message : String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    private fun moveActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}