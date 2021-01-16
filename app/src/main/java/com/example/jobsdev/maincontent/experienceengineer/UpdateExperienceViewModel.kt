package com.example.jobsdev.maincontent.experienceengineer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jobsdev.retfrofit.GeneralResponse
import com.example.jobsdev.retfrofit.JobSDevApiService
import kotlinx.coroutines.*
import retrofit2.HttpException
import kotlin.coroutines.CoroutineContext

class UpdateExperienceViewModel : ViewModel(), CoroutineScope {

    var isUpdateLivedata = MutableLiveData<Boolean>()
    var isDeleteLivedata = MutableLiveData<Boolean>()
    var isMessage = MutableLiveData<String>()
    var isLoading = MutableLiveData<Boolean>()

    private lateinit var service : JobSDevApiService

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun setService(service : JobSDevApiService) {
        this.service = service
    }

    fun callUpdateExperienceApi(exId : Int, exPosition : String, exCompany : String, exStartDate : String, exEndDate : String, exDesc : String) {
        launch {
            isLoading.value = true

            val results = withContext(Dispatchers.IO){
                try {
                    service.updateExperienceByExId(exId, exPosition, exCompany, exStartDate, exEndDate, exDesc)
                } catch (e: HttpException) {
                    isLoading.value = false

                    withContext(Dispatchers.Main) {
                        isUpdateLivedata.value = false

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
                isUpdateLivedata.value = true
                isMessage.value = results.message
                isLoading.value = false
            } else {
                isUpdateLivedata.value = false
                isMessage.value = "Something wrong ..."
                isLoading.value = false
            }
        }
    }

    fun callDeleteExpApi(exId : Int) {
        launch {
            isLoading.value = true

            val results = withContext(Dispatchers.IO){
                try {
                    service.deleteExperienceByExId(exId)
                } catch (e: HttpException) {
                    isLoading.value = false

                    withContext(Dispatchers.Main) {
                        isDeleteLivedata.value = false

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
                isDeleteLivedata.value = true
                isMessage.value = results.message
                isLoading.value = false

            } else {
                isDeleteLivedata.value = false
                isMessage.value = "Something wrong ..."
                isLoading.value = false
            }
        }
    }



}