package com.example.jobsdev.maincontent.projectcompany.listhirebyprojectid

interface DetailProjectCompanyContract {

    interface ViewDetailProjectCompany {
        fun addListHireByPjId(list : List<HireByProjectIdModel>)
        fun failedAdd(msg : String)
        fun showProgressBar()
        fun hideProgressBar()
    }

    interface PresenterDetailProjectCompany {
        fun bindView(view : ViewDetailProjectCompany)
        fun unbind()
        fun callListHireByPjIdApi()
    }
}