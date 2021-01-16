package com.example.jobsdev.maincontent.experienceengineer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jobsdev.retfrofit.GeneralResponse
import com.example.jobsdev.retfrofit.JobSDevApiService
import com.example.jobsdev.sharedpreference.ConstantAccountEngineer
import com.example.jobsdev.sharedpreference.PreferencesHelper
import kotlinx.coroutines.*
import retrofit2.HttpException
import kotlin.coroutines.CoroutineContext

class AddExperienceViewModel : ViewModel(), CoroutineScope {

    var isAddExperienceLiveData = MutableLiveData<Boolean>()
    var isMessageLiveData =  MutableLiveData<String>()

    private lateinit var service : JobSDevApiService
    private lateinit var sharedPref : PreferencesHelper

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun setSharedPref(sharedPref : PreferencesHelper) {
        this.sharedPref = sharedPref
    }

    fun setService(service : JobSDevApiService) {
        this.service = service
    }

    fun callAddExperienceApi(exPosition : String, exCompany : String, exStartDate : String, exEndDate : String, exDesc : String) {
        launch {
            val enId = sharedPref.getValueString(ConstantAccountEngineer.engineerId)
            val results = withContext(Dispatchers.IO){
                try {
                    service.addExperience(exPosition, exCompany, exStartDate, exEndDate, exDesc, enId!!.toInt())
                } catch (e: HttpException) {
                    withContext(Dispatchers.Main) {
                        isAddExperienceLiveData.value = false

                        when {
                            e.code() == 404 -> {
                                isMessageLiveData.value = "Not Found!"
                            }
                            e.code() == 400 -> {
                                isMessageLiveData.value = "expired"
                            }
                            else -> {
                                isMessageLiveData.value = "Server under maintenance!"
                            }
                        }
                    }
                }
            }

            if(results is GeneralResponse) {
                isAddExperienceLiveData.value = true
                isMessageLiveData.value = results.message
            } else {
                isAddExperienceLiveData.value = false
                isMessageLiveData.value = "Something wrong ..."
            }
        }
    }
}