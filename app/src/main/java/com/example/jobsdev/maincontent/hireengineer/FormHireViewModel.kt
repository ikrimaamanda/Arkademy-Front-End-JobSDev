package com.example.jobsdev.maincontent.hireengineer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jobsdev.sharedpreference.ConstantDetailEngineer
import com.example.jobsdev.sharedpreference.ConstantProjectCompany
import com.example.jobsdev.sharedpreference.PreferencesHelper
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class FormHireViewModel : ViewModel(), CoroutineScope {

    var isHireLiveData = MutableLiveData<Boolean>()
    var isMessage = MutableLiveData<String>()

    private lateinit var sharedPref : PreferencesHelper
    private lateinit var service : HireApiService

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun setSharedPref(sharedPref : PreferencesHelper) {
        this.sharedPref = sharedPref
    }

    fun setService(service : HireApiService) {
        this.service = service
    }

    fun callHireApi(hirePrice : String, hireMessage : String) {
        launch {
            val results = withContext(Dispatchers.IO){
                try {
                    val enId = sharedPref.getValueString(ConstantDetailEngineer.engineerId)
                    val projectId = sharedPref.getValueString(ConstantProjectCompany.projectId)
                    service.addHire(enId!!.toInt(), projectId!!.toInt(), hirePrice, hireMessage)
                } catch (e:Throwable) {
                    e.printStackTrace()

                    withContext(Dispatchers.Main) {
                        isHireLiveData.value = false
                    }
                }
            }

            if(results is HireResponse) {
                isHireLiveData.value = true
                isMessage.value = results.message
            } else {
                isHireLiveData.value = false
                isMessage.value = "Something wrong..."
            }
        }
    }


}