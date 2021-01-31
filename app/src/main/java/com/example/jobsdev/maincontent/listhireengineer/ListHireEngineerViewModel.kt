package com.example.jobsdev.maincontent.listhireengineer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jobsdev.sharedpreference.ConstantAccountEngineer
import com.example.jobsdev.sharedpreference.PreferencesHelper
import kotlinx.coroutines.*
import retrofit2.HttpException
import kotlin.coroutines.CoroutineContext

class ListHireEngineerViewModel : ViewModel(), CoroutineScope {

    val isListHireEngineerLiveData = MutableLiveData<Boolean>()
    val isAddList = MutableLiveData<List<DetailHireEngineerModel>>()
    val isLoading = MutableLiveData<Boolean>()
    val isMessage = MutableLiveData<String>()

    private lateinit var service : HireEngineerApiService
    private lateinit var sharedPref : PreferencesHelper

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun setSharedPrefHire(sharedPref : PreferencesHelper) {
        this.sharedPref = sharedPref
    }

    fun setListHireEngineerService(service : HireEngineerApiService) {
        this.service = service
    }

    fun callListHireEngineerApi() {
        launch {
            isLoading.value = true

            val response = withContext(Dispatchers.IO) {
                try {
                    service.getHireByEngineerId(sharedPref.getValueString(ConstantAccountEngineer.engineerId))
                } catch (e: HttpException) {
                    withContext(Dispatchers.Main) {
                        isLoading.value = false
                        isListHireEngineerLiveData.value = false

                        when {
                            e.code() == 404 -> {
                                isMessage.value = "Data not found!"
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

            if(response is ListHireEngineerResponse) {
                val list = response.data.map {
                    DetailHireEngineerModel(it.hireId, it.companyId, it.companyName, it.position,
                        it.companyFields, it.companyCity, it.companyDescription, it.companyInstagram,
                        it.companyLinkedIn, it.engineerId, it.projectId, it.projectName, it.projectDescription,
                        it.projectDeadline, it.projectImage, it.price, it.message, it.status, it.dateConfirm)
                }
                isListHireEngineerLiveData.value = true
                isAddList.value = list
                isLoading.value = false

            } else {
                isListHireEngineerLiveData.value = false
                isMessage.value = "Hello, your list hire is empty!"
                isLoading.value = false
            }
        }
    }

}