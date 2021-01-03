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
import com.example.jobsdev.sharedpreference.*
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

                if(result.success) {
                    Log.d("loginReq", result.toString())

                    saveSession(result.data.accountId, result.data.accountEmail, result.data.token, result.data.accountLevel)
                    if (result.data.accountLevel == 0) {
                        getEngineerId(result.data.accountId)
                    } else if (result.data.accountLevel == 1) {
                        getCompanyId(result.data.accountId)
                    }
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

    private fun getEngineerId(acId : String) {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service.getEngineerByAcId(acId)
                } catch (e:Throwable) {
                    Log.e("errorMessage : ", e.message.toString())
                    e.printStackTrace()
                }
            }

            if (result is DetailEngineerByAcIdResponse) {
                Log.d("enIdReq", result.toString())
                if (result.success) {
                    sharedPref.putValue(ConstantAccountEngineer.engineerId, result.data.engineerId!!)
                    if (result.data.enJobTitle!!.isNotEmpty() && result.data.enJobType!!.isNotEmpty() && result.data.enDescription!!.isNotEmpty() && result.data.enProfilePict!!.isNotEmpty() && result.data.enLocation!!.isNotEmpty() || result.data.accountName!!.isNotEmpty() || result.data.accountPhoneNumber!!.isNotEmpty() ) {
                        sharedPref.putValue(ConstantAccountEngineer.jobTitle, result.data.enJobTitle!!)
                        sharedPref.putValue(ConstantAccountEngineer.jobType, result.data.enJobType!!)
                        saveAccountData(result.data.enDescription!!, result.data.enProfilePict!!, result.data.accountName!!, result.data.accountPhoneNumber!!, result.data.enLocation!!)
                    }
                }
            }
        }
    }

    private fun getCompanyId(acId : String) {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service.getCompanyByAcId(acId)
                } catch (e:Throwable) {
                    Log.e("errorMessage : ", e.message.toString())
                    e.printStackTrace()
                }
            }

            if (result is DetailCompanyByAcIdResponse) {
                Log.d("cnIdReqByCom", result.toString())
                if (result.success) {
                    sharedPref.putValue(ConstantAccountCompany.companyId, result.data.companyId!!)
                    sharedPref.putValue(ConstantAccountCompany.companyName, result.data.companyName!!)
                    sharedPref.putValue(ConstantAccountCompany.position, result.data.position!!)
                    sharedPref.putValue(ConstantAccountCompany.fields, result.data.fields!!)
                    sharedPref.putValue(ConstantAccountCompany.instagram, result.data.instagram!!)
                    sharedPref.putValue(ConstantAccountCompany.linkedin, result.data.linkedin!!)
                    saveAccountData(result.data.cnDescription!!, result.data.cnProfilePict!!, result.data.accountName!!, result.data.accountPhoneNumber!!, result.data.companyCity!!)
                }
            }
        }
    }

    private fun saveAccountData(description : String, profilePict : String, name : String, phoneNumber : String, location : String) {
        sharedPref.putValue(Constant.prefDescription, description)
        sharedPref.putValue(Constant.prefProfilePict, profilePict)
        sharedPref.putValue(Constant.prefName, name)
        sharedPref.putValue(Constant.prefPhoneNumber, phoneNumber)
        sharedPref.putValue(Constant.prefLocation, location)
    }

}