package com.example.jobsdev.maincontent.account

import com.example.jobsdev.retfrofit.DetailCompanyByAcIdResponse

interface AccountCompanyContract {

    interface ViewAcCompany {
        fun setDataCompany(data : DetailCompanyByAcIdResponse.Company)
        fun failedSetData(msg : String)
        fun showProgressBar()
        fun hideProgressBar()
    }

    interface PresenterAcCompany {
        fun bindView(view : ViewAcCompany)
        fun unbindView()
        fun callCompanyIdApi()
    }
}