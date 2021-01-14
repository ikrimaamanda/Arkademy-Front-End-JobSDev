package com.example.jobsdev.maincontent.editprofile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jobsdev.retfrofit.GeneralResponse
import com.example.jobsdev.retfrofit.JobSDevApiService
import com.example.jobsdev.sharedpreference.ConstantAccountCompany
import com.example.jobsdev.sharedpreference.ConstantAccountEngineer
import com.example.jobsdev.sharedpreference.PreferencesHelper
import kotlinx.coroutines.*
import okhttp3.MultipartBody
import kotlin.coroutines.CoroutineContext

class UpdateProfilePictViewModel : ViewModel(), CoroutineScope {

    val isUpdateImageLiveData = MutableLiveData<Boolean>()
    val isMessage = MutableLiveData<String>()
    val isLoading = MutableLiveData<Boolean>()

    private lateinit var service: JobSDevApiService
    private lateinit var sharedPref : PreferencesHelper
    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun setSharedPref(sharedPref : PreferencesHelper) {
        this.sharedPref = sharedPref
    }

    fun setUpdateImageService(service : JobSDevApiService) {
        this.service = service
    }

    fun callUpdateProfilePictEngineerApi(img: MultipartBody.Part) {
        launch {
            isLoading.value = true
            val result = withContext(Dispatchers.IO) {
                try {
                    val enId = sharedPref.getValueString(ConstantAccountEngineer.engineerId)!!.toInt()
                    service.updateProfilePictEngineer(enId, img)

                } catch (e:Throwable) {
                    e.printStackTrace()
                    isLoading.value = false

                    withContext(Dispatchers.Main) {
                        isUpdateImageLiveData.value = false
                    }
                }
            }

            if(result is GeneralResponse) {
                isUpdateImageLiveData.value = true
                isLoading.value = false
                isMessage.value = result.message
            } else {
                isUpdateImageLiveData.value = false
                isLoading.value = false
                isMessage.value = "Something wrong..."
            }
        }

    }

    fun callUpdateProfilePictCompanyApi(img: MultipartBody.Part) {
        launch {
            isLoading.value = true
            val result = withContext(Dispatchers.IO) {
                try {
                    val cnId = sharedPref.getValueString(ConstantAccountCompany.companyId)!!.toInt()
                    service.updateProfilePictCompany(cnId, img)
                } catch (e:Throwable) {
                    e.printStackTrace()
                }
            }

            if(result is GeneralResponse) {
                isUpdateImageLiveData.value = true
                isLoading.value = false
                isMessage.value = result.message
            } else {
                isUpdateImageLiveData.value = false
                isLoading.value = false
                isMessage.value = "Something wrong..."
            }
        }

    }


}