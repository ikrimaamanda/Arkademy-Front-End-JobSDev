package com.example.jobsdev.maincontent.projectcompany

interface ListProjectContract {

    interface ListProjectView {
        fun addListProject(list : List<ProjectCompanyModel>)
        fun failedAdd(message : String)
        fun showProgressBar()
        fun hideProgressBar()
    }

    interface ListProjectPresenter {
        fun bindToView(view : ListProjectView)
        fun unbind()
        fun callProjectApiByCnId()
    }
}