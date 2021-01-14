package com.example.jobsdev.maincontent.listhireengineer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jobsdev.maincontent.hireengineer.HireApiService
import com.example.jobsdev.maincontent.hireengineer.HireResponse
import com.example.jobsdev.sharedpreference.PreferencesHelper
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class DetailHireEngineerViewModel : ViewModel(), CoroutineScope {

    val isUpdateStatusHireLivedata = MutableLiveData<Boolean>()
    val isMessage = MutableLiveData<String>()
    val isLoading = MutableLiveData<Boolean>()

    private lateinit var service : HireApiService
    private lateinit var sharedPref : PreferencesHelper

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun setUpdateStatusHireService(service : HireApiService) {
        this.service = service
    }

    fun callUpdateHireStatusApi(hireId : Int, status : String) {
        launch {
            isLoading.value = true
            val result = withContext(Dispatchers.IO) {
                try {
                    service.updateHireStatus(hireId, status)
                } catch (e : Throwable) {
                    e.printStackTrace()
                    isLoading.value = false

                    withContext(Dispatchers.Main) {
                        isUpdateStatusHireLivedata.value = false
                    }
                }
            }
            if (result is HireResponse) {
                if (result.success) {
                    isUpdateStatusHireLivedata.value = true
                    isLoading.value = false
                    isMessage.value = result.message
                }
            } else {
                isUpdateStatusHireLivedata.value = false
                isLoading.value = false
                isMessage.value = "Something wrong..."
            }
        }
    }

}