package com.example.jobsdev.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.jobsdev.R
import com.example.jobsdev.databinding.ActivityLoginBinding
import com.example.jobsdev.maincontent.MainContentActivity
import com.example.jobsdev.onboard.OnBoardRegLogActivity
import com.example.jobsdev.onboard.OnBoardRegisterActivity
import com.example.jobsdev.remote.ApiClient
import com.example.jobsdev.reset_password.ResetPasswordSendEmailActivity
import com.example.jobsdev.retfrofit.JobSDevApiService
import com.example.jobsdev.sharedpreference.*

class LoginActivity : AppCompatActivity() {

    private lateinit var sharedPref : PreferencesHelper
    private lateinit var binding : ActivityLoginBinding
    private lateinit var service : JobSDevApiService
    private lateinit var viewModel : LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,
            R.layout.activity_login
        )

        sharedPref = PreferencesHelper(this)
        service = ApiClient.getApiClient(context = this)!!.create(JobSDevApiService::class.java)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        viewModel.setSharedPref(sharedPref)
        viewModel.setLoginService(service)

        binding.btnLogin.setOnClickListener {
            if(binding.etEmail.text.isEmpty() || binding.etPassword.text.isEmpty()) {
                Toast.makeText(this, "Please filled all field", Toast.LENGTH_SHORT).show()
                binding.etEmail.requestFocus()
            } else {
                viewModel.callLoginApi(binding.etEmail.text.toString(), binding.etPassword.text.toString())
            }
        }

        subscribeLoginLiveData()

        binding.tvRegisterHere.setOnClickListener {
            val intentRegister = Intent(this, OnBoardRegisterActivity::class.java)
            startActivity(intentRegister)
        }

        binding.tvForgotPassword.setOnClickListener {
            val intentForgotPassword = Intent(this, ResetPasswordSendEmailActivity::class.java)
            startActivity(intentForgotPassword)
        }

    }

    private fun subscribeLoginLiveData() {
        viewModel.isLoginLiveData.observe(this, Observer { it1 ->

            if (it1) {
                viewModel.isMessage.observe(this, Observer {
                    showMessage(it)
                    moveActivity()
                })

            } else {
                viewModel.isMessage.observe(this, Observer {
                    showMessage(it)
                })
            }
        })
    }

    private fun moveActivity() {
        if (sharedPref.getValueBoolean(Constant.prefIsLogin)!!) {
            startActivity(Intent(this, MainContentActivity::class.java))
            finish()
        }
    }

    private fun showMessage(message : String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, OnBoardRegLogActivity::class.java))
    }

}