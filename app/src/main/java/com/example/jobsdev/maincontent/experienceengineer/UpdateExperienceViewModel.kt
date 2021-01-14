package com.example.jobsdev.maincontent.experienceengineer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jobsdev.retfrofit.GeneralResponse
import com.example.jobsdev.retfrofit.JobSDevApiService
import com.example.jobsdev.sharedpreference.PreferencesHelper
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class UpdateExperienceViewModel : ViewModel(), CoroutineScope {

    var isUpdateLivedata = MutableLiveData<Boolean>()
    var isDeleteLivedata = MutableLiveData<Boolean>()
    var isMessage = MutableLiveData<String>()

    private lateinit var service : JobSDevApiService

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun setService(service : JobSDevApiService) {
        this.service = service
    }

    fun callUpdateExperienceApi(exId : Int, exPosition : String, exCompany : String, exStartDate : String, exEndDate : String, exDesc : String) {
        launch {
            val results = withContext(Dispatchers.IO){
                try {
                    service.updateExperienceByExId(exId, exPosition, exCompany, exStartDate, exEndDate, exDesc)
                } catch (e:Throwable) {
                    e.printStackTrace()

                    withContext(Dispatchers.Main) {
                        isUpdateLivedata.value = false
                    }
                }
            }

            if(results is GeneralResponse) {
                isUpdateLivedata.value = true
                isMessage.value = results.message
//                moveActivity()
            } else {
                isUpdateLivedata.value = false
                isMessage.value = "Something wrong ..."
            }
        }
    }

    fun callDeleteExpApi(exId : Int) {
        launch {
            val results = withContext(Dispatchers.IO){
                try {
                    service.deleteExperienceByExId(exId)
                } catch (e:Throwable) {
                    e.printStackTrace()

                    withContext(Dispatchers.Main) {
                        isDeleteLivedata.value = false
                    }
                }
            }

            if(results is GeneralResponse) {
                isDeleteLivedata.value = true
                isMessage.value = results.message
//                moveActivity()
            } else {
                isDeleteLivedata.value = false
                isMessage.value = "Something wrong ..."
            }
        }
    }



}