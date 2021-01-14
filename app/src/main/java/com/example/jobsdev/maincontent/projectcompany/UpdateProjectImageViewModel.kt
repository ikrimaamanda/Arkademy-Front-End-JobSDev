package com.example.jobsdev.maincontent.projectcompany

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jobsdev.retfrofit.GeneralResponse
import com.example.jobsdev.sharedpreference.ConstantProjectCompany
import com.example.jobsdev.sharedpreference.PreferencesHelper
import kotlinx.coroutines.*
import okhttp3.MultipartBody
import kotlin.coroutines.CoroutineContext

class UpdateProjectImageViewModel : ViewModel(), CoroutineScope {

    val isUpdateImageLiveData = MutableLiveData<Boolean>()
    val isMessage = MutableLiveData<String>()
    val isLoading = MutableLiveData<Boolean>()

    private lateinit var service: ProjectsCompanyApiService
    private lateinit var sharedPref : PreferencesHelper
    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun setSharedPref(sharedPref : PreferencesHelper) {
        this.sharedPref = sharedPref
    }

    fun setUpdateImageService(service : ProjectsCompanyApiService) {
        this.service = service
    }

    fun callUpdateProjectImageApi(img: MultipartBody.Part) {
        launch {
            isLoading.value = true
            val result = withContext(Dispatchers.IO) {
                try {
                    val pjId = sharedPref.getValueString(ConstantProjectCompany.projectId)!!.toInt()
                    service.updateProjectImage(pjId, img)
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


}