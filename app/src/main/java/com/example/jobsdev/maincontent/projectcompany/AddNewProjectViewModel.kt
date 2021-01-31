package com.example.jobsdev.maincontent.projectcompany

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jobsdev.sharedpreference.ConstantAccountCompany
import com.example.jobsdev.sharedpreference.PreferencesHelper
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import kotlin.coroutines.CoroutineContext

class AddNewProjectViewModel : ViewModel(), CoroutineScope {

    var isCreateProjectLiveData = MutableLiveData<Boolean>()
    var isMessage = MutableLiveData<String>()
    var isLoading = MutableLiveData<Boolean>()

    private lateinit var sharedPref : PreferencesHelper
    private lateinit var service : ProjectsCompanyApiService
    private lateinit var image: MultipartBody.Part

    fun setSharedPref(sharedPref : PreferencesHelper) {
        this.sharedPref = sharedPref
    }

    fun setService(service : ProjectsCompanyApiService) {
        this.service = service
    }

    fun setImage(image : MultipartBody.Part) {
        this.image = image
    }

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun callAddProjectApi(projectName : String, projectDesc : String, projectDeadline : String) {
        launch {
            isLoading.value = true
            val results = withContext(Dispatchers.IO){
                try {
                    val cnId = sharedPref.getValueString(ConstantAccountCompany.companyId)!!.toRequestBody("text/plain".toMediaTypeOrNull())
                    val projectName = projectName
                        .toRequestBody("text/plain".toMediaTypeOrNull())
                    val projectDesc = projectDesc
                        .toRequestBody("text/plain".toMediaTypeOrNull())
                    val projectDeadline = projectDeadline
                        .toRequestBody("text/plain".toMediaTypeOrNull())

                    service.addNewProject(projectName, projectDesc, projectDeadline, image, cnId)
                } catch (e: HttpException) {
                    withContext(Dispatchers.Main) {
                        isLoading.value = false
                        isCreateProjectLiveData.value = false

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

            if(results is AddProjectResponse) {
                isCreateProjectLiveData.value = true
                isMessage.value = results.message
                isLoading.value = false
            } else {
                isCreateProjectLiveData.value = false
                isMessage.value = "Something wrong..."
                isLoading.value = false
            }
        }
    }

}