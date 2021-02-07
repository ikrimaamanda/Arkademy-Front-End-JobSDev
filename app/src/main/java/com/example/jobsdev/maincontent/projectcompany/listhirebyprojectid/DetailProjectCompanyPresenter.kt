package com.example.jobsdev.maincontent.projectcompany.listhirebyprojectid

import android.util.Log
import com.example.jobsdev.maincontent.hireengineer.HireApiService
import com.example.jobsdev.sharedpreference.ConstantProjectCompany
import com.example.jobsdev.sharedpreference.PreferencesHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class DetailProjectCompanyPresenter(private val coroutineScope: CoroutineScope,
                                    private val service : HireApiService,
                                    private val sharedPref : PreferencesHelper
) : DetailProjectCompanyContract.PresenterDetailProjectCompany {

    private var view : DetailProjectCompanyContract.ViewDetailProjectCompany? = null

    override fun bindView(view: DetailProjectCompanyContract.ViewDetailProjectCompany) {
        this.view = view
    }

    override fun unbind() {
        this.view = null
    }

    override fun callListHireByPjIdApi() {
        coroutineScope.launch {
            view?.showProgressBar()

            val response = withContext(Dispatchers.IO) {
                Log.d("listHireProject", "CallApi: ${Thread.currentThread().name}")

                try {
                    service.getListHireByProjectId(sharedPref.getValueString(ConstantProjectCompany.projectId))
                } catch (e: HttpException) {
                    withContext(Dispatchers.Main) {
                        view?.hideProgressBar()

                        when {
                            e.code() == 400 -> {
                                view?.failedAdd("expired")
                            }
                            else -> {
                            }
                        }
                    }
                }
            }

            if(response is HireByProjectResponse) {
                if (response.success) {
                    val list = response.data.map {
                        HireByProjectIdModel(it.hrId, it.cnId, it.enId, it.enJobTitle, it.enJobType, it.enProfilePict, it.projectId, it.projectName,
                            it.projectDesc, it.projectDeadline, it.projectImage, it.hirePrice, it.hireMessage, it.hireStatus, it.hireDateConfirm, it.hireCreatedAt, it.projectCreateAt, it.projectUpdateAt, it.name, it.email, it.pHoneNumber)
                    }
                    view?.addListHireByPjId(list)
                } else {
                    view?.failedAdd()
                }
            } else {
                view?.failedAdd()
            }
        }

    }


}