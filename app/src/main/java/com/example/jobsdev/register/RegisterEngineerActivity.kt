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
import com.example.jobsdev.databinding.ActivityRegisterEngineerBinding
import com.example.jobsdev.remote.ApiClient
import com.example.jobsdev.sharedpreference.PreferencesHelper
import kotlinx.coroutines.*

class RegisterEngineerActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRegisterEngineerBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service : RegistrationApiService
    private lateinit var sharedPref : PreferencesHelper
    private lateinit var viewModel : RegisterEngineerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_register_engineer)

        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(context = this)!!.create(RegistrationApiService::class.java)
        sharedPref = PreferencesHelper(this)
        viewModel = ViewModelProvider(this).get(RegisterEngineerViewModel::class.java)

        viewModel.setSharedPref(sharedPref)
        viewModel.setRegisterEngineerService(service)

        binding.btnRegister.setOnClickListener {
            if(binding.etName.text.isEmpty() || binding.etEmail.text.isEmpty() || binding.etNumberPhone.text.isEmpty() || binding.etPassword.text.isEmpty() || binding.etConfirmPassword.text.isEmpty()) {
                Toast.makeText(this, "Please filled all field", Toast.LENGTH_SHORT).show()
                binding.etName.requestFocus()
            } else if(binding.etConfirmPassword.text.toString() != binding.etPassword.text.toString()) {
                showMessage("Please write again password")
                binding.etConfirmPassword.requestFocus()
            }
            else {
                viewModel.callRegistrationApi(binding.etName.text.toString(), binding.etEmail.text.toString(), binding.etNumberPhone.text.toString(), binding.etPassword.text.toString())
            }
        }

        subscribeLiveData()

        binding.tvLoginHere.setOnClickListener {
            val intentLogin = Intent(this, LoginActivity::class.java)
            startActivity(intentLogin)
        }
    }

    private fun subscribeLiveData() {
        viewModel.isRegisterLiveData.observe( this, Observer {
            if (it) {
                viewModel.isMessage.observe(this, Observer { it1->
                    showMessage(it1)
                })
                moveActivity()
            } else {
                viewModel.isMessage.observe(this, Observer { it1->
                    showMessage(it1)
                })
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