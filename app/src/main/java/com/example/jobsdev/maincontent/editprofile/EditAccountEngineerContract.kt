package com.example.jobsdev.maincontent.editprofile

import com.example.jobsdev.retfrofit.DetailEngineerByAcIdResponse

interface EditAccountEngineerContract {

    interface ViewEditAcEngineer {
        fun setData(data : DetailEngineerByAcIdResponse.Engineer)
        fun successUpdate(msg : String)
        fun failed(msg : String)
        fun showProgressBar()
        fun hideProgressBar()
    }

    interface PresenterEditAcEngineer{
        fun bindView(view : ViewEditAcEngineer)
        fun unbind()
        fun callEngineerIdApi()
        fun callUpdateAccountApi(name : String, phoneNumber : String, password : String)
        fun callUpdateEngineerApi(jobTitle : String, location : String, description : String)
    }
}