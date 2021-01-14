package com.example.jobsdev.maincontent.skillengineer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jobsdev.retfrofit.GeneralResponse
import com.example.jobsdev.retfrofit.JobSDevApiService
import com.example.jobsdev.sharedpreference.ConstantAccountEngineer
import com.example.jobsdev.sharedpreference.PreferencesHelper
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class AddSkillViewModel : ViewModel(), CoroutineScope {

    var isAddSkillLiveData = MutableLiveData<Boolean>()
    var isMessage =  MutableLiveData<String>()

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
            val results = withContext(Dispatchers.IO){
                try {
                    service.addSkill(skillName, sharedPref.getValueString(ConstantAccountEngineer.engineerId)!!.toInt())
                } catch (e:Throwable) {
                    e.printStackTrace()

                    withContext(Dispatchers.Main) {
                        isAddSkillLiveData.value = false
                    }
                }
            }

            if(results is GeneralResponse) {
                isAddSkillLiveData.value = true
                isMessage.value = results.message
            } else {
                isAddSkillLiveData.value = false
                isMessage.value = "Something wrong ..."
            }
        }
    }

}