package com.example.jobsdev.maincontent.home

import com.example.jobsdev.maincontent.listengineer.DetailEngineerModel

interface HomeContract {

    interface ViewHome {
        fun onResultSuccess(list: List<DetailEngineerModel>)
        fun onResultFail(message: String)
        fun showLoading()
        fun hideLoading()
    }

    interface PresenterHome {
        fun bindToView(view: ViewHome)
        fun unbind()
        fun callListEngineerService()
    }
}