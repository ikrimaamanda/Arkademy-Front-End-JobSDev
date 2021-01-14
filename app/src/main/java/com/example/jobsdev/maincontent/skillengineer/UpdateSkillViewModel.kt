package com.example.jobsdev.maincontent.skillengineer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jobsdev.retfrofit.GeneralResponse
import com.example.jobsdev.retfrofit.JobSDevApiService
import com.example.jobsdev.sharedpreference.PreferencesHelper
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class UpdateSkillViewModel : ViewModel(), CoroutineScope {

    var isUpdateSkillLiveData = MutableLiveData<Boolean>()
    var isDeleteSkillLiveData = MutableLiveData<Boolean>()
    var isMessageLiveData = MutableLiveData<String>()

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

    fun callUpdateSkillApi(skillId : Int, skillName : String) {
        launch {
            val results = withContext(Dispatchers.IO){
                try {
                    service.updateSkillName(skillId, skillName)
                } catch (e:Throwable) {
                    e.printStackTrace()

                    withContext(Dispatchers.Main) {
                        isUpdateSkillLiveData.value = false
                    }
                }
            }

            if(results is GeneralResponse) {
                isUpdateSkillLiveData.value = true
                isMessageLiveData.value = results.message
            } else {
                isUpdateSkillLiveData.value = false
                isMessageLiveData.value = "Something wrong ..."
            }
        }
    }

    fun callDeleteSkillApi(skillId : Int) {
        launch {
            val results = withContext(Dispatchers.IO){
                try {
                    service.deleteSkillBySkId(skillId)
                } catch (e:Throwable) {
                    e.printStackTrace()

                    withContext(Dispatchers.Main) {
                        isDeleteSkillLiveData.value = false
                    }
                }
            }

            if(results is GeneralResponse) {
                isDeleteSkillLiveData.value = true
                isMessageLiveData.value = results.message
            } else {
                isDeleteSkillLiveData.value = false
                isMessageLiveData.value = "Something wrong ..."
            }
        }
    }


}