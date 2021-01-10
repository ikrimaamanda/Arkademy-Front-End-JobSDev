package com.example.jobsdev.maincontent.search

import com.example.jobsdev.maincontent.listengineer.DetailEngineerModel

interface SearchContract {

    interface View{
        fun onResultSuccess(list: List<DetailEngineerModel>)
        fun onResultFail(message: String)
        fun showLoading()
        fun hideLoading()
    }

    interface Presenter {
        fun bindToView(view: View)
        fun unbind()
        fun callServiceSearch(search: String?)
        fun callServiceFilter(filter: Int?)
    }
}