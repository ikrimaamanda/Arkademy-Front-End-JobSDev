package com.example.jobsdev.maincontent.projectcompany

interface ListProjectContract {

    interface ListProjectView {
        fun addListProject(list : List<ProjectCompanyModel>)
        fun showProgressBar(msg : String)
    }

    interface ListProjectPresenter {
        fun bindToView(view : ListProjectView)
        fun unbind()
        fun callProjectApiByCnId()
    }
}