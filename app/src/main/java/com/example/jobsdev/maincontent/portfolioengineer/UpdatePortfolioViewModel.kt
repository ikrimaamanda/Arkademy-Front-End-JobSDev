package com.example.jobsdev.maincontent.portfolioengineer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jobsdev.retfrofit.GeneralResponse
import com.example.jobsdev.retfrofit.JobSDevApiService
import com.example.jobsdev.sharedpreference.ConstantPortfolio
import com.example.jobsdev.sharedpreference.PreferencesHelper
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import kotlin.coroutines.CoroutineContext

class UpdatePortfolioViewModel : ViewModel(), CoroutineScope {

    var isUpdatePortfolioLiveData = MutableLiveData<Boolean>()
    var isDeletePortfolioLiveData = MutableLiveData<Boolean>()
    var isMessage = MutableLiveData<String>()
    var isLoading = MutableLiveData<Boolean>()

    private lateinit var sharedPref : PreferencesHelper
    private lateinit var service : JobSDevApiService

    fun setSharedPref(sharedPref : PreferencesHelper) {
        this.sharedPref = sharedPref
    }

    fun setService(service : JobSDevApiService) {
        this.service = service
    }

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun callUpdatePortfolioApi(portfolioId : Int, prAppName : String, prDesc : String, prLinkPub : String, prLinkRepo : String, prWorkplace : String) {
        launch {
            isLoading.value = true
            val results = withContext(Dispatchers.IO){
                try {
                    val prAppName = prAppName
                        .toRequestBody("text/plain".toMediaTypeOrNull())
                    val prDesc = prDesc
                        .toRequestBody("text/plain".toMediaTypeOrNull())
                    val prLinkPub = prLinkPub
                        .toRequestBody("text/plain".toMediaTypeOrNull())
                    val prLinkRepo = prLinkRepo
                        .toRequestBody("text/plain".toMediaTypeOrNull())
                    val prWorkplace = prWorkplace
                        .toRequestBody("text/plain".toMediaTypeOrNull())
                    val prType = sharedPref.getValueString(ConstantPortfolio.typeApp)!!
                        .toRequestBody("text/plain".toMediaTypeOrNull())

                    service.updatePortfolio(portfolioId, prAppName, prDesc, prLinkPub, prLinkRepo, prWorkplace, prType)

                } catch (e:Throwable) {
                    e.printStackTrace()
                    isLoading.value = false

                    withContext(Dispatchers.Main) {
                        isUpdatePortfolioLiveData.value = false
                    }
                }
            }

            if(results is GeneralResponse) {
                isUpdatePortfolioLiveData.value = true
                isMessage.value = results.message
                isLoading.value = false
            } else {
                isUpdatePortfolioLiveData.value = false
                isMessage.value = "Something wrong..."
                isLoading.value = false
            }
        }
    }

    fun callDeletePortfolioApi(portfolioId : Int) {
        launch {
            isLoading.value = true
            val response = withContext(Dispatchers.IO) {
                try {
                    service.deletePortfolioByPrId(portfolioId)
                } catch (e: Throwable) {
                    e.printStackTrace()
                    isLoading.value = false

                    withContext(Dispatchers.Main) {
                        isDeletePortfolioLiveData.value = false
                    }
                }
            }

            if(response is GeneralResponse) {
                isDeletePortfolioLiveData.value = true
                isMessage.value = response.message
                isLoading.value = false
            } else {
                isDeletePortfolioLiveData.value = false
                isMessage.value = "Something wrong..."
                isLoading.value = false
            }
        }
    }

}