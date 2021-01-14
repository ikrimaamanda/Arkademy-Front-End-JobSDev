package com.example.jobsdev.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jobsdev.sharedpreference.PreferencesHelper
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class RegisterEngineerViewModel : ViewModel(), CoroutineScope {
    val isRegisterLiveData = MutableLiveData<Boolean>()

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

    fun callRegistrationApi(name : String, email : String, phoneNumber: String, password : String) {
        launch {
            val result =  withContext(Dispatchers.IO) {
                try {
                    service.registrationEngineerReq(name, email, phoneNumber, password, 0 )
                } catch (e : Throwable) {
                    e.printStackTrace()

                    withContext(Dispatchers.Main) {
                        isRegisterLiveData.value = false
                    }
                }
            }

            if(result is RegistrationResponse) {
                isRegisterLiveData.value = result.success
            } else {
                isRegisterLiveData.value = false
            }
        }
    }

}