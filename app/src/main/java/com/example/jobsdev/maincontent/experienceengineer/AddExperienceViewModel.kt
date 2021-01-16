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
    var isLoadingLiveData = MutableLiveData<Boolean>()

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
            isLoadingLiveData.value = true

            val enId = sharedPref.getValueString(ConstantAccountEngineer.engineerId)
            val results = withContext(Dispatchers.IO){
                try {
                    service.addExperience(exPosition, exCompany, exStartDate, exEndDate, exDesc, enId!!.toInt())
                } catch (e: HttpException) {
                    isLoadingLiveData.value = false

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
                isLoadingLiveData.value = false
            } else {
                isAddExperienceLiveData.value = false
                isMessageLiveData.value = "Something wrong ..."
                isLoadingLiveData.value = false
            }
        }
    }
}