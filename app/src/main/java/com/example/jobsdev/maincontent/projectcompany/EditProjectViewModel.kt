package com.example.jobsdev.maincontent.projectcompany

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jobsdev.retfrofit.GeneralResponse
import com.example.jobsdev.sharedpreference.ConstantProjectCompany
import com.example.jobsdev.sharedpreference.PreferencesHelper
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import kotlin.coroutines.CoroutineContext

class EditProjectViewModel : ViewModel(), CoroutineScope {

    val isUpdateLiveData = MutableLiveData<Boolean>()
    val isDeleteLiveData = MutableLiveData<Boolean>()
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

    fun callUpdateProjectApi(projectName : String, projectDesc : String,  projectDeadline : String) {
        launch {
            isLoading.value = true
            val response = withContext(Dispatchers.IO) {
                try {
                    val projectId = sharedPref.getValueString(ConstantProjectCompany.projectId)
                    val projectName = projectName
                        .toRequestBody("text/plain".toMediaTypeOrNull())
                    val projectDesc = projectDesc
                        .toRequestBody("text/plain".toMediaTypeOrNull())
                    val projectDeadline = projectDeadline
                        .toRequestBody("text/plain".toMediaTypeOrNull())

                    service.updateProjectById(projectId!!.toInt(), projectName, projectDesc, projectDeadline)
                } catch (e: HttpException) {
                    isLoading.value = false

                    withContext(Dispatchers.Main) {
                        isUpdateLiveData.value = false

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

            if (response is GeneralResponse) {
                isUpdateLiveData.value = true
                isLoading.value = false
                isMessage.value = response.message
            } else {
                isUpdateLiveData.value = false
                isLoading.value = false
                isMessage.value = "Something wrong..."
            }
        }
    }

    fun callDeleteProjectApi() {
        launch {
            isLoading.value = true
            val result = withContext(Dispatchers.IO) {
                try {
                    service.deleteProjectById(sharedPref.getValueString(ConstantProjectCompany.projectId)!!.toInt())
                } catch (e: HttpException) {
                    isLoading.value = false

                    withContext(Dispatchers.Main) {
                        isDeleteLiveData.value = false

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

            if (result is GeneralResponse) {
                isDeleteLiveData.value = true
                isLoading.value = false
                isMessage.value = result.message
            } else {
                isDeleteLiveData.value = false
                isLoading.value = false
                isMessage.value = "Something wrong..."
            }
        }
    }

}