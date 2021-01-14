package com.example.jobsdev.maincontent.editprofile

import android.util.Log
import com.example.jobsdev.retfrofit.DetailEngineerByAcIdResponse
import com.example.jobsdev.retfrofit.GeneralResponse
import com.example.jobsdev.retfrofit.JobSDevApiService
import com.example.jobsdev.sharedpreference.Constant
import com.example.jobsdev.sharedpreference.ConstantAccountEngineer
import com.example.jobsdev.sharedpreference.PreferencesHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException

class EditAccountEngineerPresenter(private val coroutineScope: CoroutineScope,
                                   private val service : JobSDevApiService,
                                   private val sharedPref : PreferencesHelper
) : EditAccountEngineerContract.PresenterEditAcEngineer {

    private var view : EditAccountEngineerContract.ViewEditAcEngineer? = null

    override fun bindView(view: EditAccountEngineerContract.ViewEditAcEngineer) {
        this.view = view
    }

    override fun unbind() {
        this.view = null
    }

    override fun callEngineerIdApi() {
        coroutineScope.launch {
            view?.showProgressBar()
            val result = withContext(Dispatchers.IO) {
                try {
                    service?.getEngineerByAcId(sharedPref.getValueString(Constant.prefAccountId))
                } catch (e: HttpException) {
                    withContext(Dispatchers.Main) {
                        view?.hideProgressBar()
                        when {
                            e.code() == 404 -> {
                                view?.failed("Data not found!")
                            }
                            e.code() == 400 -> {
                                view?.failed("expired")
                            }
                            else -> {
                                view?.failed("Server under maintenance!")
                            }
                        }
                    }
                }
            }

            if (result is DetailEngineerByAcIdResponse) {
                if (result.success) {
                    Log.d("engineerResponse", result.toString())
                    view?.setData(result.data)
                } else {
                    view?.failed(result.message)
                }
            } else {
                view?.failed("Something wrong...")
            }
        }

    }

    override fun callUpdateAccountApi(acName  : String, acPhoneNumber  : String, acPassword : String) {
        coroutineScope.launch {
            val response = withContext(Dispatchers.IO) {
                try {
                    service.updateAccountByAcId(sharedPref.getValueString(Constant.prefAccountId)!!.toInt(), acName, acPhoneNumber, acPassword)
                } catch (e: HttpException) {
                    withContext(Dispatchers.Main) {
                        view?.hideProgressBar()
                        when {
                            e.code() == 404 -> {
                                view?.failed("Data not found!")
                            }
                            e.code() == 400 -> {
                                view?.failed("expired")
                            }
                            else -> {
                                view?.failed("Server under maintenance!")
                            }
                        }
                    }
                }
            }

            if (response is GeneralResponse) {
                if (response.success) {
                    view?.successUpdate(response.message)
                } else {
                    view?.failed(response.message)
                }
            } else{
                view?.failed("Something wrong...")
            }
        }
    }

    override fun callUpdateEngineerApi(jobTitle : String, location : String, description : String) {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    val enId = sharedPref.getValueString(ConstantAccountEngineer.engineerId)!!.toInt()
                    val jobTitle = jobTitle.toRequestBody("text/plain".toMediaTypeOrNull())
                    val jobType = sharedPref.getValueString(ConstantAccountEngineer.jobType)!!
                        .toRequestBody("text/plain".toMediaTypeOrNull())
                    val location = location.toRequestBody("text/plain".toMediaTypeOrNull())
                    val description = description.toRequestBody("text/plain".toMediaTypeOrNull())

                    service.updateEngineerById(enId, jobTitle, jobType, location, description)

                } catch (e: HttpException) {
                    withContext(Dispatchers.Main) {
                        view?.hideProgressBar()
                        when {
                            e.code() == 404 -> {
                                view?.failed("Data not found!")
                            }
                            e.code() == 400 -> {
                                view?.failed("expired")
                            }
                            else -> {
                                view?.failed("Server under maintenance!")
                            }
                        }
                    }
                }
            }

            if(result is GeneralResponse) {
                if (result.success) {
                    view?.successUpdate(result.message)
                } else {
                    view?.failed(result.message)
                }
            } else {
                view?.failed("Something wrong...")
            }
        }
    }


}