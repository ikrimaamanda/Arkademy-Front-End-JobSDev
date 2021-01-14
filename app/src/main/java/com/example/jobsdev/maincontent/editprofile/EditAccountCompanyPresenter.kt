package com.example.jobsdev.maincontent.editprofile

import android.util.Log
import com.example.jobsdev.retfrofit.DetailCompanyByAcIdResponse
import com.example.jobsdev.retfrofit.DetailEngineerByAcIdResponse
import com.example.jobsdev.retfrofit.GeneralResponse
import com.example.jobsdev.retfrofit.JobSDevApiService
import com.example.jobsdev.sharedpreference.Constant
import com.example.jobsdev.sharedpreference.ConstantAccountCompany
import com.example.jobsdev.sharedpreference.ConstantAccountEngineer
import com.example.jobsdev.sharedpreference.PreferencesHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException

class EditAccountCompanyPresenter(private val coroutineScope: CoroutineScope,
                                  private val service : JobSDevApiService,
                                  private val sharedPref : PreferencesHelper
) : EditAccountCompanyContract.PresenterEditAcCompany {

    private var view : EditAccountCompanyContract.ViewEditAcCompany? = null

    override fun bindView(view: EditAccountCompanyContract.ViewEditAcCompany) {
        this.view = view
    }

    override fun unbind() {
        this.view = null
    }

    override fun callCompanyIdApi() {
        coroutineScope.launch {
            view?.showProgressBar()
            val response = withContext(Dispatchers.IO) {
                try {
                    service?.getCompanyByAcId(sharedPref.getValueString(Constant.prefAccountId))
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

            if (response is DetailCompanyByAcIdResponse) {
                if (response.success) {
                    view?.setData(response.data)
                } else {
                    view?.failed(response.message)
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

    override fun callUpdateCompanyApi(position : String, fields : String, location : String, description : String, instagram : String, linkedin : String, companyName : String) {
        coroutineScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    val cnId = sharedPref.getValueString(ConstantAccountCompany.companyId)!!.toInt()
                    val position = position
                        .toRequestBody("text/plain".toMediaTypeOrNull())
                    val fields = fields
                        .toRequestBody("text/plain".toMediaTypeOrNull())
                    val location = location
                        .toRequestBody("text/plain".toMediaTypeOrNull())
                    val description = description
                        .toRequestBody("text/plain".toMediaTypeOrNull())
                    val instagram = instagram
                        .toRequestBody("text/plain".toMediaTypeOrNull())
                    val linkedin = linkedin
                        .toRequestBody("text/plain".toMediaTypeOrNull())
                    val companyName = companyName
                        .toRequestBody("text/plain".toMediaTypeOrNull())

                    service.updateCompany(cnId, position, fields, location, description, instagram, linkedin, companyName)

                } catch (e: HttpException) {
                    withContext(Dispatchers.Main) {
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
                if(result.success) {
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