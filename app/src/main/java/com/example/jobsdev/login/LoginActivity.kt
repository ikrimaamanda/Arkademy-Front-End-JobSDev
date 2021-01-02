package com.example.jobsdev.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.jobsdev.R
import com.example.jobsdev.databinding.ActivityLoginBinding
import com.example.jobsdev.maincontent.MainContentActivity
import com.example.jobsdev.onboard.OnBoardRegLogActivity
import com.example.jobsdev.onboard.OnBoardRegisterActivity
import com.example.jobsdev.remote.ApiClient
import com.example.jobsdev.reset_password.ResetPasswordSendEmailActivity
import com.example.jobsdev.sharedpreference.Constant
import com.example.jobsdev.sharedpreference.PreferencesHelper
import kotlinx.coroutines.*

class LoginActivity : AppCompatActivity() {

    private lateinit var sharedPref : PreferencesHelper
    private lateinit var binding : ActivityLoginBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service : AuthApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,
            R.layout.activity_login
        )

        sharedPref = PreferencesHelper(this)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(context = this)!!.create(AuthApiService::class.java)

        binding.btnLogin.setOnClickListener {
            if(binding.etEmail.text.isEmpty() || binding.etPassword.text.isEmpty()) {
                Toast.makeText(this, "Please filled all field", Toast.LENGTH_SHORT).show()
                binding.etEmail.requestFocus()
            } else {
                callLoginApi(binding.etEmail.text.toString(), binding.etPassword.text.toString())
                sharedPref.putValue(Constant.prefPassword, binding.etPassword.text.toString())

            }
        }

        binding.tvRegisterHere.setOnClickListener {
            val intentRegister = Intent(this, OnBoardRegisterActivity::class.java)
            startActivity(intentRegister)
        }

        binding.tvForgotPassword.setOnClickListener {
            val intentForgotPassword = Intent(this, ResetPasswordSendEmailActivity::class.java)
            startActivity(intentForgotPassword)
        }

    }

    private fun callLoginApi(email : String, password : String) {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service.loginRequest(email, password)
                } catch (e : Throwable) {
                    e.printStackTrace()
                }
            }

            if(result is LoginResponse) {
                Log.d("loginReq", result.toString())

                if(result.success) {
                    saveSession(result.data.accountId, result.data.accountEmail, result.data.token, result.data.accountLevel)
                    showMessage(result.message)
                    moveActivity()
                }
            } else {
                showMessage("Email / password not registered")
            }
        }
    }

    private fun moveActivity() {
        if (sharedPref.getValueBoolean(Constant.prefIsLogin)!!) {
            startActivity(Intent(this, MainContentActivity::class.java))
            finish()
        }
    }

    private fun saveSession(accountId : String, email : String, token : String, level : Int) {
        sharedPref.putValue(Constant.prefAccountId, accountId)
        sharedPref.putValue(Constant.prefEmail, email)
        sharedPref.putValue(Constant.prefToken, token)
        sharedPref.putValue(Constant.prefLevel, level)
        sharedPref.putValue(Constant.prefIsLogin, true)
    }

    private fun showMessage(message : String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, OnBoardRegLogActivity::class.java))
    }

    private fun getEngineerId() {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {

                } catch (e:Throwable) {
                    Log.e("errorMessage : ", e.message.toString())
                    e.printStackTrace()
                }
            }
        }
    }

}