package com.example.jobsdev.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jobsdev.sharedpreference.PreferencesHelper
import kotlinx.coroutines.*
import retrofit2.HttpException
import kotlin.coroutines.CoroutineContext

class RegisterCompanyViewModel : ViewModel(), CoroutineScope {

    val isRegisterCompanyLiveData = MutableLiveData<Boolean>()
    val isMessage = MutableLiveData<String>()

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
                } catch (e: HttpException) {
                    withContext(Dispatchers.Main) {
                        isRegisterCompanyLiveData.value = false

                        when {
                            e.code() == 404 -> {
                                isMessage.value = "Not Found!"
                            }
                            e.code() == 400 -> {
                                isMessage.value = "expired"
                            }
                            else -> {
                                isMessage.value = "Server under maintenance!"
                            }
                        }
                    }
                }
            }

            if(result is RegistrationResponse) {
                isRegisterCompanyLiveData.value = result.success
                isMessage.value = result.message
            } else {
                isRegisterCompanyLiveData.value = false
                isMessage.value = "Registration failed!"
            }
        }
    }

}