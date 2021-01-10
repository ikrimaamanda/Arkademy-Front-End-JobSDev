package com.example.jobsdev.maincontent.listhireengineer

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jobsdev.maincontent.fragment.ListHireEngineerView
import com.example.jobsdev.sharedpreference.ConstantAccountEngineer
import com.example.jobsdev.sharedpreference.PreferencesHelper
import kotlinx.coroutines.*
import retrofit2.HttpException
import kotlin.coroutines.CoroutineContext

class ListHireEngineerViewModel : ViewModel(), CoroutineScope {

    private var view : ListHireEngineerView? = null

    val isListHireEngineerLiveData = MutableLiveData<Boolean>()

    private lateinit var service : HireEngineerApiService
    private lateinit var sharedPref : PreferencesHelper

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun bindToView(view: ListHireEngineerView) {
        this.view = view
    }

    fun unbind() {
        this.view = null
    }

    fun setSharedPrefHire(sharedPref : PreferencesHelper) {
        this.sharedPref = sharedPref
    }

    fun setListHireEngineerService(service : HireEngineerApiService) {
        this.service = service
    }

    fun callListHireEngineerApi() {
        launch {
            view?.showProgressBar()

            val response = withContext(Dispatchers.IO) {
                try {
                    service?.getHireByEngineerId(sharedPref.getValueString(ConstantAccountEngineer.engineerId))
                } catch (e: HttpException) {
                    withContext(Dispatchers.Main) {
                        view?.hideProgressBar()

                        when {
                            e.code() == 404 -> {
                                view?.failedAdd("No data engineer!")
                            }
                            e.code() == 400 -> {
                                view?.failedAdd("expired")
                            }
                            else -> {
                                view?.failedAdd("Server under maintenance!")
                            }
                        }
                    }
                }
            }

            if(response is ListHireEngineerResponse) {

                if (response.success) {
                    val list = response.data?.map {
                        DetailHireEngineerModel(it.hireId, it.companyId, it.companyName, it.position,
                            it.companyFields, it.companyCity, it.companyDescription, it.companyInstagram,
                            it.companyLinkedIn, it.engineerId, it.projectId, it.projectName, it.projectDescription,
                            it.projectDeadline, it.projectImage, it.price, it.message, it.status, it.dateConfirm)
                    }
                    view?.addListHireEngineer(list)
                    isListHireEngineerLiveData.value = true
                } else {
                    view?.failedAdd(response.message)
                }
            } else {
                isListHireEngineerLiveData.value = false
                view?.failedAdd("Hello, your list hire is empty!")
            }
        }
    }

}