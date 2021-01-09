package com.example.jobsdev.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.jobsdev.login.LoginActivity
import com.example.jobsdev.R
import com.example.jobsdev.databinding.ActivityRegisterEngineerBinding
import com.example.jobsdev.remote.ApiClient
import kotlinx.coroutines.*

class RegisterEngineerActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRegisterEngineerBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service : RegistrationApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_register_engineer)

        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(context = this)!!.create(RegistrationApiService::class.java)

        binding.btnRegister.setOnClickListener {
            if(binding.etName.text.isEmpty() || binding.etEmail.text.isEmpty() || binding.etNumberPhone.text.isEmpty() || binding.etPassword.text.isEmpty() || binding.etConfirmPassword.text.isEmpty()) {
                Toast.makeText(this, "Please filled all field", Toast.LENGTH_SHORT).show()
                binding.etName.requestFocus()
            } else if(binding.etConfirmPassword.text.toString() != binding.etPassword.text.toString()) {
                showMessage("Please write again password")
                binding.etConfirmPassword.requestFocus()
            }
            else {
                callRegistrationApi(binding.etName.text.toString(), binding.etEmail.text.toString(), binding.etNumberPhone.text.toString(), binding.etPassword.text.toString())
            }
        }

        binding.tvLoginHere.setOnClickListener {
            val intentLogin = Intent(this, LoginActivity::class.java)
            startActivity(intentLogin)
        }
    }

    private fun callRegistrationApi(name : String, email : String, phoneNumber: String, password : String) {
        coroutineScope.launch {
            val result =  withContext(Dispatchers.IO) {
                try {
                    service.registrationEngineerReq(name, email, phoneNumber, password, 0 )
                } catch (e : Throwable) {
                    e.printStackTrace()
                }
            }

            if(result is RegistrationResponse) {
                Log.d("registrationReq : ", result.toString())

                if(result.success) {
                    showMessage(result.message)
                    moveActivity()
                } else {
                    showMessage(result.message)
                }
            } else {
                showMessage("Email already registered")
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