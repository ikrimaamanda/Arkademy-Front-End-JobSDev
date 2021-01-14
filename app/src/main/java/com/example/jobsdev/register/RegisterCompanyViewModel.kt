package com.example.jobsdev.register

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jobsdev.sharedpreference.PreferencesHelper
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class RegisterCompanyViewModel : ViewModel(), CoroutineScope {

    val isRegisterCompanyLiveData = MutableLiveData<Boolean>()

    private lateinit var service : RegistrationApiService
    private lateinit var sharedPref : PreferencesHelper

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun setSharedPref(sharedPref : PreferencesHelper) {
        this.sharedPref = sharedPref
    }

    fun setRegisterEngineerService(service: RegistrationApiService) {
        this.service = service
    }

    fun callRegistrationApi(name : String, email : String, phoneNumber: String, password : String, companyName : String, position : String) {
        launch {
            val result =  withContext(Dispatchers.IO) {
                try {
                    service.registrationCompanyReq(name, email, phoneNumber, password, 1, companyName, position)
                } catch (e : Throwable) {
                    e.printStackTrace()

                    withContext(Dispatchers.Main) {
                        isRegisterCompanyLiveData.value = false
                    }
                }
            }

            if(result is RegistrationResponse) {
                Log.d("registrationComReq : ", result.toString())

                isRegisterCompanyLiveData.value = result.success
            } else {
                isRegisterCompanyLiveData.value = false
            }
        }
    }

}