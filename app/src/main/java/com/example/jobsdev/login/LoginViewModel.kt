package com.example.jobsdev.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jobsdev.retfrofit.DetailCompanyByAcIdResponse
import com.example.jobsdev.retfrofit.DetailEngineerByAcIdResponse
import com.example.jobsdev.retfrofit.JobSDevApiService
import com.example.jobsdev.sharedpreference.Constant
import com.example.jobsdev.sharedpreference.ConstantAccountCompany
import com.example.jobsdev.sharedpreference.ConstantAccountEngineer
import com.example.jobsdev.sharedpreference.PreferencesHelper
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class LoginViewModel : ViewModel(), CoroutineScope {

    val isLoginLiveData = MutableLiveData<Boolean>()
    val isMessage = MutableLiveData<String>()
    val isGetEngineerId = MutableLiveData<Boolean>()
    val isGetCompanyId = MutableLiveData<Boolean>()

    private lateinit var service : JobSDevApiService
    private lateinit var sharedPref : PreferencesHelper

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun setSharedPref(sharedPref : PreferencesHelper) {
        this.sharedPref = sharedPref
    }

    fun setLoginService(service: JobSDevApiService) {
        this.service = service
    }

    fun callLoginApi(email : String, password : String) {
        launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service.loginRequest(email, password)
                } catch (e : Throwable) {
                    e.printStackTrace()

                    withContext(Dispatchers.Main) {
                        isLoginLiveData.value = false
                    }
                }
            }

            if(result is LoginResponse) {

                if(result.success) {
                    saveSession(result.data.accountId, result.data.accountEmail, result.data.token, result.data.accountLevel)
                    sharedPref.putValue(Constant.prefPassword, password)
                    isLoginLiveData.value = true
                    isMessage.value = result.message

                    if (result.data.accountLevel == 0) {
                        getEngineerId(result.data.accountId)
                    } else if (result.data.accountLevel == 1) {
                        getCompanyId(result.data.accountId)
                    }

                } else {
                    isLoginLiveData.value = false
                    isMessage.value = result.message
                }
            } else {
                isLoginLiveData.value = false
                isMessage.value = "Email / password not registered"
            }
        }
    }

    private fun saveSession(accountId : String, email : String, token : String, level : Int) {
        sharedPref.putValue(Constant.prefAccountId, accountId)
        sharedPref.putValue(Constant.prefEmail, email)
        sharedPref.putValue(Constant.prefToken, token)
        sharedPref.putValue(Constant.prefLevel, level)
        sharedPref.putValue(Constant.prefIsLogin, true)
    }

    private fun getEngineerId(acId : String) {
        launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service.getEngineerByAcId(acId)
                } catch (e:Throwable) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main) {
                        isGetEngineerId.value = false
                    }
                }
            }

            if (result is DetailEngineerByAcIdResponse) {
                if (result.success) {
                    sharedPref.putValue(ConstantAccountEngineer.engineerId, result.data.engineerId!!)
                    sharedPref.putValue(Constant.prefName, result.data.accountName!!)
                    sharedPref.putValue(Constant.prefPhoneNumber, result.data.accountPhoneNumber!!)

                    isGetEngineerId.value = true
                } else {
                    isGetEngineerId.value = false
                }
            }
        }
    }

    private fun getCompanyId(acId : String) {
        launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    service.getCompanyByAcId(acId)
                } catch (e: Throwable) {
                    e.printStackTrace()

                    withContext(Dispatchers.Main) {
                        isGetCompanyId.value = false
                    }
                }
            }

            if (result is DetailCompanyByAcIdResponse) {
                if (result.success) {
                    sharedPref.putValue(ConstantAccountCompany.companyId, result.data.companyId!!)
                    sharedPref.putValue(Constant.prefName, result.data.accountName!!)
                    sharedPref.putValue(Constant.prefPhoneNumber, result.data.accountPhoneNumber!!)
                    sharedPref.putValue(ConstantAccountCompany.companyName, result.data.companyName!!)
                    sharedPref.putValue(ConstantAccountCompany.position, result.data.position!!)

                    isGetCompanyId.value = true
                } else {
                    isGetCompanyId.value = false
                }
            }
        }
    }

}