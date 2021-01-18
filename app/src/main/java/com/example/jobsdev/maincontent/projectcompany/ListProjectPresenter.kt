package com.example.jobsdev.maincontent.projectcompany

import com.example.jobsdev.sharedpreference.ConstantAccountCompany
import com.example.jobsdev.sharedpreference.PreferencesHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class ListProjectPresenter(private val coroutineScope: CoroutineScope,
                           private val service: ProjectsCompanyApiService?,
                           private val sharedPref : PreferencesHelper) : ListProjectContract.ListProjectPresenter {

    private var view : ListProjectContract.ListProjectView? = null

    override fun bindToView(view: ListProjectContract.ListProjectView) {
        this.view = view
    }

    override fun unbind() {
        this.view = null
    }

    override fun callProjectApiByCnId() {
        coroutineScope.launch {

            view?.showProgressBar()

            val response = withContext(Dispatchers.IO) {
                try {
                    service?.getProjectByCnId(sharedPref.getValueString(ConstantAccountCompany.companyId))
                } catch (e: HttpException) {
                    withContext(Dispatchers.Main) {
                        view?.hideProgressBar()

                        when {
                            e.code() == 404 -> {
                                view?.failedAdd("Data not found!")
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

            if(response is ProjectResponse) {
                if (response.success) {
                    val list = response.data.map {
                        ProjectCompanyModel(it.projectId, it.companyId, it.projectName, it.projectDesc, it.projectDeadline, it.projectImage, it.projectCreateAt, it.projectUpdateAt)
                    }
                    view?.addListProject(list)
                } else {
                    view?.failedAdd(response.message)
                }
            } else {
                view?.failedAdd("Hello, your list project is empty!")
            }
        }
    }
}