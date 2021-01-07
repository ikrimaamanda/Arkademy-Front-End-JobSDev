package com.example.jobsdev.maincontent.projectcompany

import android.util.Log
import com.example.jobsdev.sharedpreference.ConstantAccountCompany
import com.example.jobsdev.sharedpreference.PreferencesHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
            val response = withContext(Dispatchers.IO) {
                try {
                    service?.getProjectByCnId(sharedPref.getValueString(ConstantAccountCompany.companyId))
                } catch (e:Throwable) {
                    e.printStackTrace()
                }
            }

            Log.d("Ikrima Response", response.toString())

            if(response is ProjectResponse) {
                view?.showProgressBar("Hello, These is your list project")
                val list = response.data?.map {
                    ProjectCompanyModel(it.projectId, it.companyId, it.projectName, it.projectDesc, it.projectDeadline, it.projectImage, it.projectCreateAt, it.projectUpdateAt)
                }
                view?.addListProject(list)

            } else {
                view?.showProgressBar("Hello, your list project is empty!")
            }
        }
    }
}