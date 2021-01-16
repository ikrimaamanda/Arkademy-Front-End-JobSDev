package com.example.jobsdev.maincontent.skillengineer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jobsdev.retfrofit.GeneralResponse
import com.example.jobsdev.retfrofit.JobSDevApiService
import com.example.jobsdev.sharedpreference.PreferencesHelper
import kotlinx.coroutines.*
import retrofit2.HttpException
import kotlin.coroutines.CoroutineContext

class UpdateSkillViewModel : ViewModel(), CoroutineScope {

    var isUpdateSkillLiveData = MutableLiveData<Boolean>()
    var isDeleteSkillLiveData = MutableLiveData<Boolean>()
    var isMessageLiveData = MutableLiveData<String>()
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

    fun callUpdateSkillApi(skillId : Int, skillName : String) {
        launch {
            isLoadingLiveData.value = true

            val results = withContext(Dispatchers.IO){
                try {
                    service.updateSkillName(skillId, skillName)
                } catch (e: HttpException) {
                    isLoadingLiveData.value = false

                    withContext(Dispatchers.Main) {
                        isUpdateSkillLiveData.value = false

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
                isUpdateSkillLiveData.value = true
                isMessageLiveData.value = results.message
                isLoadingLiveData.value = false
            } else {
                isUpdateSkillLiveData.value = false
                isMessageLiveData.value = "Something wrong ..."
                isLoadingLiveData.value = false
            }
        }
    }

    fun callDeleteSkillApi(skillId : Int) {
        launch {
            isLoadingLiveData.value = true

            val results = withContext(Dispatchers.IO){
                try {
                    service.deleteSkillBySkId(skillId)
                } catch (e: HttpException) {
                    isLoadingLiveData.value = false

                    withContext(Dispatchers.Main) {
                        isDeleteSkillLiveData.value = false

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
                isDeleteSkillLiveData.value = true
                isMessageLiveData.value = results.message
                isLoadingLiveData.value = false
            } else {
                isDeleteSkillLiveData.value = false
                isMessageLiveData.value = "Something wrong ..."
                isLoadingLiveData.value = false
            }
        }
    }


}