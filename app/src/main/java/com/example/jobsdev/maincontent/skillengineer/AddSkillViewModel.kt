package com.example.jobsdev.maincontent.skillengineer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jobsdev.retfrofit.GeneralResponse
import com.example.jobsdev.retfrofit.JobSDevApiService
import com.example.jobsdev.sharedpreference.ConstantAccountEngineer
import com.example.jobsdev.sharedpreference.PreferencesHelper
import kotlinx.coroutines.*
import retrofit2.HttpException
import kotlin.coroutines.CoroutineContext

class AddSkillViewModel : ViewModel(), CoroutineScope {

    var isAddSkillLiveData = MutableLiveData<Boolean>()
    var isMessage =  MutableLiveData<String>()
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

    fun callAddSkillApi(skillName : String) {
        launch {
            isLoadingLiveData.value = true
            val results = withContext(Dispatchers.IO){
                try {
                    service.addSkill(skillName, sharedPref.getValueString(ConstantAccountEngineer.engineerId)!!.toInt())
                } catch (e: HttpException) {
                    isLoadingLiveData.value = false

                    withContext(Dispatchers.Main) {
                        isAddSkillLiveData.value = false

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

            if(results is GeneralResponse) {
                isAddSkillLiveData.value = true
                isMessage.value = results.message
                isLoadingLiveData.value = false
            } else {
                isAddSkillLiveData.value = false
                isMessage.value = "Something wrong ..."
                isLoadingLiveData.value = false
            }
        }
    }

}