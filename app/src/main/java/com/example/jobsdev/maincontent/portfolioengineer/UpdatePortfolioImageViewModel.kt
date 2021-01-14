package com.example.jobsdev.maincontent.portfolioengineer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jobsdev.retfrofit.GeneralResponse
import com.example.jobsdev.retfrofit.JobSDevApiService
import com.example.jobsdev.sharedpreference.PreferencesHelper
import kotlinx.coroutines.*
import okhttp3.MultipartBody
import kotlin.coroutines.CoroutineContext

class UpdatePortfolioImageViewModel : ViewModel(), CoroutineScope {

    var isUpdatePortfolioImageLiveData = MutableLiveData<Boolean>()
    var isMessage = MutableLiveData<String>()
    var isLoading = MutableLiveData<Boolean>()

    private lateinit var service : JobSDevApiService

    fun setService(service : JobSDevApiService) {
        this.service = service
    }

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun callUpdatePortfolioImageApi(portfolioId : Int, image : MultipartBody.Part) {
        launch {
            isLoading.value = true
            val results = withContext(Dispatchers.IO){
                try {
                    service.updatePortfolioImage(portfolioId, image)

                } catch (e:Throwable) {
                    e.printStackTrace()
                    isLoading.value = false

                    withContext(Dispatchers.Main) {
                        isUpdatePortfolioImageLiveData.value = false
                    }
                }
            }

            if(results is GeneralResponse) {
                isUpdatePortfolioImageLiveData.value = true
                isMessage.value = results.message
                isLoading.value = false
            } else {
                isUpdatePortfolioImageLiveData.value = false
                isMessage.value = "Something wrong..."
                isLoading.value = false
            }
        }
    }

}