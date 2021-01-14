package com.example.jobsdev.maincontent.editprofile

import com.example.jobsdev.retfrofit.DetailCompanyByAcIdResponse

interface EditAccountCompanyContract {

    interface ViewEditAcCompany {
        fun setData(data : DetailCompanyByAcIdResponse.Company)
        fun successUpdate(msg : String)
        fun failed(msg : String)
        fun showProgressBar()
        fun hideProgressBar()
    }

    interface PresenterEditAcCompany{
        fun bindView(view : ViewEditAcCompany)
        fun unbind()
        fun callCompanyIdApi()
        fun callUpdateAccountApi(name : String, phoneNumber : String, password : String)
        fun callUpdateCompanyApi(position : String, fields : String, location : String,
                                 description : String, instagram : String, linkedin : String, companyName : String)
    }
}