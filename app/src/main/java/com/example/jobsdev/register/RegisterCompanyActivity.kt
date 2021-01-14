package com.example.jobsdev.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.jobsdev.login.LoginActivity
import com.example.jobsdev.R
import com.example.jobsdev.databinding.ActivityRegisterCompanyBinding
import com.example.jobsdev.remote.ApiClient
import com.example.jobsdev.sharedpreference.PreferencesHelper
import kotlinx.coroutines.*

class RegisterCompanyActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRegisterCompanyBinding
    private lateinit var sharedPref : PreferencesHelper
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service : RegistrationApiService
    private lateinit var viewModel : RegisterCompanyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register_company)

        sharedPref = PreferencesHelper(this)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(context = this)!!.create(RegistrationApiService::class.java)
        viewModel = ViewModelProvider(this).get(RegisterCompanyViewModel::class.java)

        viewModel.setSharedPref(sharedPref)
        viewModel.setRegisterEngineerService(service)

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
                viewModel.callRegistrationApi(binding.etName.text.toString(), binding.etEmail.text.toString(), binding.etNumberPhone.text.toString(), binding.etPassword.text.toString(), binding.etCompany.text.toString(), binding.etPosition.text.toString())
            }
        }

        subscribeLiveData()

    }

    private fun subscribeLiveData() {
        viewModel.isRegisterCompanyLiveData.observe( this, Observer {
            if (it) {
                showMessage("Registration Success!")
                moveActivity()
            } else {
                showMessage("Registration failed!")
            }
        })
    }

    private fun showMessage(message : String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    private fun moveActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}