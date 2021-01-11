package com.example.jobsdev.maincontent.account

import com.example.jobsdev.retfrofit.DetailCompanyByAcIdResponse
import com.example.jobsdev.retfrofit.JobSDevApiService
import com.example.jobsdev.sharedpreference.Constant
import com.example.jobsdev.sharedpreference.PreferencesHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class AccountCompanyPresenter(private val coroutineScope: CoroutineScope,
                              private val service : JobSDevApiService,
                              private val sharedPref : PreferencesHelper
) : AccountCompanyContract.PresenterAcCompany{

    private var view : AccountCompanyContract.ViewAcCompany? = null

    override fun bindView(view: AccountCompanyContract.ViewAcCompany) {
        this.view = view
    }

    override fun unbindView() {
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
                                view?.failedSetData("Data not found!")
                            }
                            e.code() == 400 -> {
                                view?.failedSetData("expired")
                            }
                            else -> {
                                view?.failedSetData("Server under maintenance!")
                            }
                        }
                    }
                }
            }

            if (response is DetailCompanyByAcIdResponse) {
                if (response.success) {
                    view?.setDataCompany(response.data)
                    if (response.data.cnProfilePict != null) {
                        sharedPref.putValue(Constant.prefProfilePict, response.data.cnProfilePict!!)
                    }
                } else {
                    view?.failedSetData(response.message)
                }
            } else {
                view?.failedSetData("Hello, your data is empty")
            }
        }

    }


}