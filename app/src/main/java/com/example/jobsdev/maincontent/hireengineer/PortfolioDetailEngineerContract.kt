package com.example.jobsdev.maincontent.hireengineer

import com.example.jobsdev.maincontent.portfolioengineer.ItemPortfolioModel


interface PortfolioDetailEngineerContract {

    interface ViewPortfolioDetailEngineer{
        fun addListPortfolioDetailEngineer(list : List<ItemPortfolioModel>)
        fun failedAdd(msg : String)
        fun showProgressBar()
        fun hideProgressBar()
    }

    interface PresenterPortfolioDetailEngineer {
        fun bindView(view : ViewPortfolioDetailEngineer)
        fun unbind()
        fun callListPortfolioApi()
    }
}